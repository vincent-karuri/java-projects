# A simple web app built using Spring and deployed to Tomcat

This project implements a simple web app that can be deployed to tomcat.

The web app part follows along this [tutorial](https://medium.com/@yuntianhe/create-a-web-project-with-maven-spring-mvc-b859503f74d7).

The project also includes configuration files that can be used when deploying the web app on port 80 of a remote or local tomcat server instance.

The project also contains Packer and Ansible scripts that can be used to build an AWS AMI with Tomcat installed, configured and running on port 80. A Jenkins file, which can be used to automate builds from git branches and to deploy the resulting war file to a running server, is also included.

Other references when installing tomcat on Ubuntu are [here](https://www.linode.com/docs/development/frameworks/apache-tomcat-on-ubuntu-16-04/), [here](https://brianflove.com/2014/06/04/simple-tomcat7-install-on-ubuntu-10/), [here](https://askubuntu.com/questions/1067160/tomcat-7-as-a-service-under-systemctl-in-18-04-configurationsolved
), [here](https://askubuntu.com/a/314614), [here](https://askubuntu.com/a/703674) and [here](https://askubuntu.com/a/79566).
