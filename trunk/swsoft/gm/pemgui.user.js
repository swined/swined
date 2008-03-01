defDomain = '.bcom';

// ---

function rids() {
    return 'sw-'+(1000+Math.floor(Math.random()*8999));
}

function rid() {
	return 'auto-'+rids();
}

function rdom() {
    return rid()+defDomain;
}

// ---

function geId(id) {
	return document.getElementById(id);
}

function geName(name) {
	return document.getElementsByName(name);
}

function geName0(name) {
	return geName(name).item(0);
}

// ---

if (geId('screenID')) {
    var sid = geId('screenID').innerHTML;
    
    // subscription period select
    if ((sid == 'This screen ID: 01.13.65') || (sid == 'This screen ID: 01.13.75')) {
	var links = document.links; 
        for (var i=0; links[i]; i++) 
	    if (links[i].innerHTML == 'Year(s)') 
		location.replace('javascript:' + links[i].getAttribute('onClick'));
    }

    // subscription params select
    if ((sid == 'This screen ID:01.17.14') || (sid == 'This screen ID:01.17.24')) {
	var planname = geId('input___Planname');
	var planval = planname.value;
	var pername = geId('input___PlanPeriodPeriod');
	var perval = pername.value;
	geId('input___refPlan').click();
	window.setInterval(function () {
	    if (planname.value != planval) 
		geId('input___refPlanPeriod').click();
	    planval = planname.value;		
	    if (pername.value != perval) 
		geId('input___SaveAdd').click();
	    perval = pername.value;
	}, 1000)
    }
    
    // place order
    if ((sid == 'This screen ID: 01.17.01') || (sid == 'This screen ID: 01.17.11')) 
        geId('input___SP_ViewOrder').click();
	
    if ((sid == 'This screen ID: 01.17.21') || (sid == 'This screen ID: 01.17.19')) {
	// view order
	if (geId('input___Open_Order'))
            geId('input___Open_Order').click();
	
	// check provisioning status
	if (geId('input___Check_Order_Provisioning_Status'))
	    window.setInterval(function () {
		geId('input___Check_Order_Provisioning_Status').click();
	    },10000);
    }
    
    // set SB password
    if (sid == 'Screen ID:2.11.37.22') {
	geName0('pass').value = '123qwe';
	geName0('pass2').value = '123qwe';
    }
    
    // go to "Orders"
//    if (sid == 'This screen ID:00.02.71') {
//	var links = document.getElementsByTagName('td'); 
//        for (var i=0; links[i]; i++) 
//	    if (links[i].innerHTML == 'Orders') 
//		location.replace(links[i].getAttribute('onClick'));
//    }
    
    // select plesk hostname
    if (sid == 'This screen ID:10.20.0.0.1') 
	geId('input___DomainName').value = rdom();
    
    // select linux shared hostname 
    if (sid == 'Screen ID:2.11.11.53') {
            geName0('domain').value = rdom();
	}
	// oscomm admin pass select
	if (sid == 'Screen ID:2.11.16.10') {
		geName0('adm_pass').value = '123qwe';
		geName0('adm_pass2').value = '123qwe';
	}

	// oscomm db setup
	if (sid == 'Screen ID:2.11.16.11') {
		geName0('DB_NAME').value = rid();
		geName0('DB_USER').value = rids();
	       geName0('DB_USER_PASS').value = '123qwe';
	       geName0('DB_USER_PASS5').value = '123qwe';
	}


    
	// set certs
	if (sid == 'Screen ID:2.11.16.12') {
		var cert = '-----BEGIN CERTIFICATE-----\n\
MIICeTCCAeKgAwIBAwIEQkDvIzANBgkqhkiG9w0BAQQFADCBgDELMAkGA1UEBhMC\n\
VVMxDzANBgNVBAgTBkFsYXNrYTENMAsGA1UEBxMEQ2l0eTENMAsGA1UEChMETmFt\n\
ZTENMAsGA1UECxMEVW5pdDETMBEGA1UEAxMKZG9tYWluLmNvbTEeMBwGCSqGSIb3\n\
DQEJARYPbWFpbEBkb21haW4uY29tMB4XDTA1MDMyMzA0MjI1OFoXDTA3MDMyMzA0\n\
MjI1OFowgYAxCzAJBgNVBAYTAlVTMQ8wDQYDVQQIEwZBbGFza2ExDTALBgNVBAcT\n\
BENpdHkxDTALBgNVBAoTBE5hbWUxDTALBgNVBAsTBFVuaXQxEzARBgNVBAMTCmRv\n\
bWFpbi5jb20xHjAcBgkqhkiG9w0BCQEWD21haWxAZG9tYWluLmNvbTCBnzANBgkq\n\
hkiG9w0BAQEFAAOBjQAwgYkCgYEAzIS06hXZp5kM7SerIt5YPT3fEAC/LOD6d9hR\n\
vU3txRVlHPHRYYAy18pKg7SixZhXuvuvYN3oz+5RNOUta8CxnNRoWRIUdpz9yU6P\n\
yjd4WwRcIfSDmS/aym4ubVUp5RChsqdJoNrkSfJd5QtnmrmgsQmmu4FEVgvd/Jbi\n\
9UwrrGECAwEAATANBgkqhkiG9w0BAQQFAAOBgQBbWD260JtZ02zLMZAfeBZOfIhl\n\
cgHMe928sqQvzRwgo9kAaIOgSFaqUe2BnoTW7+c3puKNWQeTLjbh4ZtRmqhSg5aY\n\
O5v7t5VO8iMvDD8Qhr/P78wjGG1y7w9UP17ZSYMh/TgXFYE5dBGuSIUGq+H/xwwr\n\
wecDivm83s2E606YkA==\n\
-----END CERTIFICATE-----';
		var key = '-----BEGIN RSA PRIVATE KEY-----\n\
MIICWwIBAAKBgQDMhLTqFdmnmQztJ6si3lg9Pd8QAL8s4Pp32FG9Te3FFWUc8dFh\n\
gDLXykqDtKLFmFe6+69g3ejP7lE05S1rwLGc1GhZEhR2nP3JTo/KN3hbBFwh9IOZ\n\
L9rKbi5tVSnlEKGyp0mg2uRJ8l3lC2eauaCxCaa7gURWC938luL1TCusYQIDAQAB\n\
AoGAdBw3ddp32m3K5+0ofMBMymAQ/s3du4XyzQ1zwoy2m7h80kT7gWY990LysPlh\n\
m9rUm+R9+fmiTkJXujcOZWldq82Swn1C7pO4Dyac0rk17AawxXFppbD6YoH0ZXGv\n\
oQswv08jmWywE9NB8LxMksgvzhPKjBG5zRV2+xBosxCOttECQQD/NITpQ6mCbSW1\n\
uXLykqMpLlpk0WIQIJGqlaQ9mlukwGkFJ9jvrEO/lEoJRxjRUxfw9YmTFjzhBwCu\n\
wCf8cqdlAkEAzSfGGH/OUS8vg0Krvyv8gT1Cc4pz37DXIqaLKuox9WvSUhXxVkZa\n\
RJwEDpeZpYSFzLUTmX8mqjd27tiSnNxXTQJARJgKLWRpWEwb+ac51SdCwZva4gRB\n\
VlJatL354jDt9u9ciu016t2jbAfginPYoNKzualVa/prLJSkQ1QUq07p3QJACL1x\n\
Cwom1/6Sn5zvhC1zjG33rsRvO1BlWeyOcT2Y80OsqiKVLX/ByLp80cJToWGL8bSl\n\
KRzpoMbMIlDHrjcU4QJAX25jPJduuUVkDYWCRgGkimmPb+SkxcCi+XlJOdjtMuFy\n\
vxOoZ1HB018TfIzbSqqIqmVhBiF+f6/ztPGzIfUvZQ==\n\
-----END RSA PRIVATE KEY-----';
		geName0('cert_text').value = cert;
		geName0('key_text').value = key;
		geName0('ca_text').value = cert;
	}

	// select new acc type
	if (sid == 'This screen ID:00.02.52') 
		geId('input___Save').click();

	// create account
	if (sid == 'This screen ID:00.02.50') {
		var cid = rid();
		GM_setValue('pem_last_user', cid); 
		geId('input___Company').value = cid;
		geId('input___Zip').value = '63009';
		geId('input___City').value = 'Novosibirsk';
		geId('input___Address1').value = 'Musi Jalilya 17';
		geId('input___AdminFName').value = 'Alexey';
		geId('input___AdminLName').value = 'Alexandrov';
		geId('input___AdminEmail').value = 'swined@pemqa.pem.plesk.ru';
		geId('input___AdminPhAreaCode').value = '12';
		geId('input___AdminPhNumber').value = '345';
		geId('input___AdminPhExtention').value = '6789';
		geId('input___CopyBillFromAdmin').click();
		geId('input___CopyTechFromAdmin').click();
		geId('input___Save').click();
	}

	// new account login settings
//	if (sid == 'This screen ID:02.21.16') {
	if (sid == 'This screen ID:02.20.83') {
		var lid = GM_getValue('pem_last_user'); ;
		geId('input___Login').value = lid;
		geId('input___Password').value = '123qwe';
		geId('input___ConfirmPassword').value = '123qwe';
		geId('input___SP_ViewAccount').click();
	}

	// usersearchfix
	if (sid == 'This screen ID: 00.02.82') {
		geId('input___CompanyName').value = 'auto-sw-*';
	}

	// WSH MB setup
	if (sid == 'Screen ID:2.52.02.02') {
		var mid = rid();
		geName0('mailbox_user_name').value = mid; 
		geName0('mailbox_display_name').value = mid;
		geName0('com.plesk.p2.cp.util.PasswordInfo_password').value = '123qwe';
		geName0('com.plesk.p2.cp.util.PasswordInfo_confirm_password').value = '123qwe';
		geName0('email_prefix').value = mid;
		geName0('exchange.mailbox.access.HTTP').click();
		geName0('exchange.mailbox.access.POP3').click();
	}

	// wsh db setup 
	if (sid == 'Screen ID:2.11.35.03') {
		var did = rid();
		geName0('db_name').value = did;
		geName0('user_name').value = did;
		geName0('com.plesk.p2.cp.ext.sql.db.modules.screens.DBUserMult_password').value = '123qwe';
		geName0('com.plesk.p2.cp.ext.sql.db.modules.screens.DBUserMult_confirm_password').value = '123qwe';
	}

	// place cancellation order
	if (sid == 'This screen ID: 00.02.93') 
		geId('input___Save2').click();
}

