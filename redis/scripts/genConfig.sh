#!/bin/bash

set -euo pipefail

if [ "$#" -ne 3 ]; then
  echo "Usage: clusterConfName ,cluster-announce-ip, port"
  exit 1
fi

confFile="$1"
clusterAnnounceIp="$2"
port="$3"
busPort=1"$3"

USE_ADMIN_USER="${REDIS_ADMIN_USER:-admin}"
USE_ADMIN_PASSWORD="${REDIS_ADMIN_PASSWORD:-adminpassword}"

USE_APP_USER="${REDIS_APP_USER:-appUser}"
USE_APP_PASSWORD="${REDIS_APP_PASSWORD:-appPassword}"

USE_REPLICATION_USER="${REDIS_REPLICATION_USER:-replicationUser}"
USE_REPLICATION_PASSWORD="${REDIS_REPLICATION_PASSWORD:-replicationPassword}"

filePath="//usr/local/etc/redis"

mkdir -p "$filePath"
cd "$filePath"

 
cat <<EOF > redis.conf
loadmodule /opt/redis-stack/lib/redisearch.so
bind 0.0.0.0
protected-mode yes
enable-debug-command yes
loglevel debug
port $port
tcp-backlog 511
timeout 0
tcp-keepalive 300
daemonize no
databases 16
set-proc-title yes
proc-title-template "{title} {listen-addr} {server-mode}"
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes
dbfilename dump.rdb
dir ./
masterauth $USE_REPLICATION_PASSWORD
masteruser $USE_REPLICATION_USER
replica-serve-stale-data no
replica-read-only yes
repl-diskless-sync yes
repl-diskless-load on-empty-db
repl-disable-tcp-nodelay no
repl-backlog-size 20mb
repl-backlog-ttl 600
replica-priority 100
acllog-max-len 128
aclfile /usr/local/etc/redis/users.acl
maxmemory 1073741824
maxmemory-policy allkeys-lfu
maxmemory-samples 8
maxmemory-eviction-tenacity 10
replica-ignore-maxmemory yes
active-expire-effort 1
lazyfree-lazy-eviction yes
lazyfree-lazy-expire yes
lazyfree-lazy-server-del yes
replica-lazy-flush yes
lazyfree-lazy-user-del yes
lazyfree-lazy-user-flush yes
oom-score-adj no
oom-score-adj-values 0 200 800
disable-thp yes
appendonly yes
appendfilename "appendonly.aof"
appenddirname "appendonlydir"
appendfsync everysec
no-appendfsync-on-rewrite no
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
aof-load-truncated yes
aof-use-rdb-preamble yes
aof-timestamp-enabled no
shutdown-timeout 20
shutdown-on-sigint default
shutdown-on-sigterm default
lua-time-limit 5000
busy-reply-threshold 5000
cluster-enabled yes
cluster-config-file ./$confFile
cluster-node-timeout 15000
cluster-replica-validity-factor 5
cluster-migration-barrier 1
cluster-require-full-coverage no
cluster-allow-reads-when-down no
cluster-allow-pubsubshard-when-down no
cluster-announce-ip $clusterAnnounceIp 
cluster-announce-port $port
cluster-announce-bus-port $busPort
slowlog-log-slower-than 10000
slowlog-max-len 128
latency-monitor-threshold 0
notify-keyspace-events ""
hash-max-listpack-entries 512
hash-max-listpack-value 64
list-max-listpack-size -2
list-compress-depth 0
set-max-intset-entries 512
set-max-listpack-entries 128
set-max-listpack-value 64
zset-max-listpack-entries 128
zset-max-listpack-value 64
hll-sparse-max-bytes 3000
stream-node-max-bytes 4096
stream-node-max-entries 100
activerehashing yes
client-output-buffer-limit normal 0 0 0
client-output-buffer-limit replica 256mb 64mb 60
client-output-buffer-limit pubsub 32mb 8mb 60
client-query-buffer-limit 3gb
maxmemory-clients 20%
hz 30
dynamic-hz yes
aof-rewrite-incremental-fsync yes
rdb-save-incremental-fsync yes
jemalloc-bg-thread yes

tls-port $port
port 0
tls-cert-file /etc/redis/certs/server.crt
tls-key-file /etc/redis/certs/server.key
tls-ca-cert-file /etc/redis/certs/ca.pem
tls-cluster yes
tls-auth-clients no
tls-replication yes
EOF

cat <<EOF > users.acl
user default off
user $USE_ADMIN_USER on >$USE_ADMIN_PASSWORD +@all ~*
user $USE_REPLICATION_USER on >$USE_REPLICATION_PASSWORD ~* +AUTH +REPLCONF +PSYNC +INFO +PING +ECHO +SELECT +CLIENT +SCRIPT +CONFIG +COMMAND
user $USE_APP_USER on >$USE_APP_PASSWORD ~* +@write +@read +@script +PING +ECHO +SELECT
EOF


redis-server "$filePath/redis.conf"

echo "Conf was created and started up"
