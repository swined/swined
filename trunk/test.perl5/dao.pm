package dao;

sub selectrow_hashref_by_field {
	my ($table, $field, $value) = @_;
	$dbh->selectrow_hashref(
		sprintf 
			'SELECT * FROM %s WHERE %s = %s', 
			map { $dbh->quote($_) }, 
				$table, 
				$field, 
				$value,
	);
}

sub load {
	selectrow_hashref_by_field('user', 'id', shift);
}

sub save {
	
}

sub delete {
	
}
