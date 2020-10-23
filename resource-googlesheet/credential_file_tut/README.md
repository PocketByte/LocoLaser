# Google Sheet Credential File

To create credentioal file, first of all you should go to [Google Developement Console](https://console.developers.google.com/project) and create a new project.

Then go to the Service Accounts page.<br>
![](google_credentials_tut_1.png)

On Service Accounts page press "CREATE SERVICE ACCOUNT".<br>
![](google_credentials_tut_2.png)

On opened page fill all required fields and press "CREATE".<br>
![](google_credentials_tut_3_1.png)

In case of LocoLaser no need to complete other steps. Just press "DONE".<br>
![](google_credentials_tut_3_2.png)

Go back to Service Accounts page. Press on created Service account.<br>
![](google_credentials_tut_4.png)

In opened page with Service account details press "ADD KEY", and then "Create new key".<br>
![](google_credentials_tut_5_1.png)

Dialog will be shown. Select JSON type and press "CREATE".<br>
![](google_credentials_tut_5_2.png)

Credentials file will be downloaded automatically.
 
On page with Service account details copy Email address.<br>
![](google_credentials_tut_6.png)

Go to Google Sheet. In the main menu go to "File/Share".<br>
![](google_credentials_tut_7_1.png)

Insert copied Service account email.<br>
![](google_credentials_tut_7_2.png)

Select role "Editor" if you want to upload strings into Sheet, otherwise "Viewer". Switch off checkbox "Notify people" and press Share.<br>
![](google_credentials_tut_7_3.png)

Done! Now you can use credential file of Service account in LocoLaser.