Mongo TLS single-node
=====================

This folder provides a simple single-node MongoDB with TLS enabled for local development.

Quick start:

1. Generate certs (creates `mongo/tls/certs`):

   ```bash
   cd mongo/tls/scripts
   ./generate-certs.sh
   ```

2. Start the container (project root):

   ```bash
   docker compose -f mongo/tls/docker-compose.yml up -d
   ```

3. Create a user (from project root):

   ```bash
   ./mongo/tls/scripts/create-user.sh appuser s3cret '[{role:"readWrite",db:"appdb"}]' admin
   ```

Connect using `mongosh` (example from host with certs available):

```bash
mongosh --host localhost --port 27017 --tls --tlsCertificateKeyFile mongo/tls/certs/mongo.pem --tlsCAFile mongo/tls/certs/ca.pem -u appuser -p s3cret --authenticationDatabase admin
```

Notes:
- `generate-certs.sh` creates a CA and a server cert with SANs for localhost and 127.0.0.1.
- Keep the `certs/` directory out of version control if you check this into a repo.
- If you prefer to run cert generation in Docker, adapt the script or use an ephemeral container that mounts the repo.

