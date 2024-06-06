workingDir=$(pwd)

echo "Working Directory: $workingDir"

sudo java -jar  -Dosgi.requiredJavaVersion=1.8 -Dosgi.instance.area.default=@user.home/eclipse-workspace -XX:+UseG1GC -XX:+UseStringDeduplication -Dosgi.requiredJavaVersion=11 -Dosgi.dataAreaRequiresExplicitInit=true -Xms1g -Xmx16g fhir-to-omop.jar $1 $2