cd productcomposite/
mvn package -DskipTests
cd ..
cd productcore/
mvn package -DskipTests
cd ..
cd usercore/
mvn package -DskipTests
cd ..
cd categorycore/
mvn package -DskipTests
cd ..
cd turbine/
mvn package -DskipTests
cd ..
cd zuul/
mvn package -DskipTests
docker-compose up --force-recreate --build -d