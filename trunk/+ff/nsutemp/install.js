const APP_AUTHOR		= "Alexey Alexandrov";
const APP_NAME			= "NSUTemp";
const APP_CHROME_NAME		= "nsutemp";
const APP_VERSION		= "0.0.1";
const APP_FILE 			= "chrome/nsutemp.jar";
const APP_FILE2 		= "nsutemp.jar";
const APP_XPCOM_SERVICE = "nsutemp.js";
const APP_CONTENTS_PATH		= "content/";

initInstall(APP_NAME, APP_CHROME_NAME, APP_VERSION); 

var chromeFolder = getFolder("Profile", "chrome");
setPackageFolder(chromeFolder);
error = addFile(APP_AUTHOR, APP_VERSION, APP_FILE, chromeFolder, null);

var jarFolder = getFolder(chromeFolder, APP_FILE2);
registerChrome(CONTENT | PROFILE_CHROME, jarFolder, APP_CONTENTS_PATH);
registerChrome(LOCALE | PROFILE_CHROME, jarFolder, APP_LOCALE_ENUS_PATH);
registerChrome(LOCALE | PROFILE_CHROME, jarFolder, APP_LOCALE_FRFR_PATH);

var result = getLastError(); 
if (result == SUCCESS) 
{
	var err = addFile(APP_NAME, ".autoreg", getFolder("Program"), "");

	error = performInstall();
	
	if (error != SUCCESS && error != REBOOT_NEEDED)
	{
		displayError(error);
		cancelInstall(error);
	}
} 
else 
{
	cancelInstall(result);
}

function displayError(error)
{
    // If the error code was -215
    if(error == READ_ONLY)
    {
        alert("The installation of " + APP_NAME +
            " failed.\nOne of the files being overwritten is read-only.");
    }
    // If the error code was -235
    else if(error == INSUFFICIENT_DISK_SPACE)
    {
        alert("The installation of " + APP_NAME +
            " failed.\nThere is insufficient disk space.");
    }
    // If the error code was -239
    else if(error == CHROME_REGISTRY_ERROR)
    {
        alert("The installation of " + APP_NAME +
            " failed.\nChrome registration failed.");
    }
    else
    {
        alert("The installation of " + APP_NAME +
            " failed.\nThe error code is: " + error);
    }
}
