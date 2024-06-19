# The fhir-to-omop Project
<hr>
<a href="https://nachc-cad.github.io/fhir-to-omop/index.html">Documentation Main Page</a>


# Introduction
<hr>>
The fhir-to-omop project started as an initial effort to map FHIR data to OMOP data.  This project grew into a series of tools that includes mapping FHIR to OMOP but also includes a suite of tools designed to address many of the common tasks associated with both FHIR and OMOP.  All of the tools here are publicly available under the Apache 2 license. Documentation for this project is available <a href="https://nachc-cad.github.io/fhir-to-omop/index.html">here</a>.  Our getting started guide is available <a href="https://nachc-cad.github.io/fhir-to-omop/pages/navbar/getting-started/start-here/StartHere.html">here</a>.  This page also includes the <a href="https://nachc-cad.github.io/fhir-to-omop/pages/navbar/getting-started/start-here/StartHere.html">prerequisites</a> required for the fhir-to-omop tools.  These tools were developed and tested in a Windows environment but should be runnable anywhere as they are all written in Java.  There are some convenience files that make running these tools easier on Windows (e.g. .bat files). These tools have been developed and tested using MS Sql Server.  

<hr>

# Installation

This installation procedure was tested on an AWS EC2 instance running on the AWS Linux operating system.

## Clone `fhir-to-omop` repo and set to correct commit
```
git clone https://github.com/sitkum-systems/fhir-to-omop.git
git reset --hard b44fe472aed835e19a216185d97c5ae0f4f9295e
cd fhir-to-omop
```

## Run the setup script to install dependencies and build `fhir-to-omop`
`sudo bash setup.sh`

# Running `fhir-to-omop`

## Start up the Broadsea Docker container for the Postgres server, etc
```
cd Broadsea
docker-compose pull && docker-compose --profile default up -d
cd ..
```
*Note* you can later use the following command to stop the Broadsea container:
`docker-compose --profile default down`

## Set the application configuration
Settings for the application can be altered in the `app.properties` file within the `fhir-to-omop` repo.

## Run `fhir-to-omop` using the provided script
```
cd /fhir-to-omop/target/fhir-to-omop/
bash fhir-to-omop.sh i
```
