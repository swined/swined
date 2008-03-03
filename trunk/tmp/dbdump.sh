#!/bin/sh

kms_sb_name="kynetique"

perl -MDBIx::DBSchema -e 'DBIx::DBSchema->new_odbc("dbi:mysql:database=$kms_db_name", "root", "$MYSQL_ROOT_PASSWD")->save("/dev/stdout")' | uuencode -m dbschema > dbschema.base64