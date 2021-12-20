#!/bin/bash
#
# Restbox launcher.
#
# @author TSS

app=''
version=''
db=''

PS3="Restbox ${version} launcher, choose the option: "
options=("Run Restbox with bugs" "Run Restbox without bugs" "Clean database" "Exit")

select option in "${options[@]}"; do
  case $option in
    "Run Restbox with bugs")
      echo "Running Restbox with bugs on port 8080..."
      java -jar ${app}-${version}.jar --spring.profiles.active=invalid
      exit 0
      ;;

    "Run Restbox without bugs")
      echo "Running Restbox without bugs on port 8081..."
      java -jar ${app}-${version}.jar --spring.profiles.active=valid
      exit 0
      ;;

    "Clean database")
      echo "Cleaning database..."
      rm -rf $db
      echo "Cleaning completed"
      exit 0
      ;;

    "Exit")
      exit 0
      ;;

    *)
      "Invalid option, exiting..."
      exit 1
      ;;
  esac
done
