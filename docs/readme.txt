Konfiguracja Tomcat'a

W pliku: $TOMCATA_HOME/conf/tomcat-users.xml, dodać:

  <role rolename="admin-gui"/>
  <role rolename="manager-gui"/>
  <role rolename="manager-script"/>
  <user username="admin" password="admin" roles="admin-gui,manager-gui,manager-script"/>

****************************************************************************************

Konfiguracja MAVEN

Do pliku: $HOME_MAVEN/conf/settings.xml, dodać:

<server>
        <id>TomcatServer</id>
        <username>admin</username>
        <password>admin</password>
</server>

gdzie, username i password, to te same dane, które należy ustawić dla użytkownika w serwerze Tomcat.
