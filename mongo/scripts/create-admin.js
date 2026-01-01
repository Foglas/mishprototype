// create-admin.js
var admin = db.getSiblingDB("admin");
admin.createUser({
    user: "<REPLACE_USER>",
    pwd: "<REPLACE_PWD>",
    roles: [ { role: "root", db: "admin" } ]
});
