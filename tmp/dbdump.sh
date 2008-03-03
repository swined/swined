#!/bin/sh

kms_db_name="kynetique"
DUMP=`perl -MDBIx::DBSchema -e "DBIx::DBSchema->new_odbc('dbi:mysql:database=$kms_db_name', 'root', '$MYSQL_ROOT_PASSWD')->save('/dev/stdout')" | uuencode -m dbschema`
cat > dbsync.sh <<EOF
#!/bin/sh
kms_db_name="$kms_db_name"
echo "$DUMP" | uudecode /dev/stdout |\\
perl -MDBIx::DBSchema -e "print \"\\$_\\n\" for DBIx::DBSchema->new_odbc('dbi:mysql:database=\$kms_db_name', 'root', '\$MYSQL_ROOT_PASSWD')->sql_update_schema(load DBIx::DBSchema '/dev/stdin')"
EOF
chmod +x dbsync.sh