function geId(id) {
	return document.getElementById(id);
}

function geName(name) {
	return document.getElementsByName(name);
}

function geName0(name) {
	return geName(name).item(0);
}

// ===================================================

function tsbn(name, value) {
	if (geName(name)) 
		geName0(name).value = value;
}

tsbn('company_name', 'swined inc');
tsbn('address1', 'Musi Jalila 17-303');
tsbn('city', 'Novosibirsk');
tsbn('postal_code', '63000');
tsbn('country', 'ru');

//tsbn('first_name_admin', 'Alexey');
//tsbn('last_name_admin', 'Alexandrov');
//tsbn('email_admin', 'swined@pemqa.pem.plesk.ru');
//tsbn('phone_country_admin', '12');
//tsbn('phone_area_admin', '345');
//tsbn('phone_number_admin', '6789');
