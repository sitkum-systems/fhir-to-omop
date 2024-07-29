if (!require("remotes")) install.packages("remotes")

# Refer to https://github.com/OHDSI/WebAPI/wiki/CDM-Configuration
# for more info on how to use this demo to add new data to Atlas

# To install the master branch
# Note: This code isn't compatible with R notebooks.
remotes::install_github("OHDSI/Achilles")

library(Achilles)
connectionDetails <- createConnectionDetails(
  dbms="postgresql", 
  server="broadsea-atlasdb/postgres", 
  user="postgres", 
  password='mypass', 
  port="5432")

Achilles::achilles(
  cdmVersion = "5.4", 
  connectionDetails = connectionDetails,
  cdmDatabaseSchema = "synthea_demo",
  resultsDatabaseSchema = "synthea_demo_ach_res"
)
