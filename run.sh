./gradlew clean build
rm -rf results
mkdir results
java -jar build/libs/PathImageSegmentation-TCO-17-1.0-SNAPSHOT.jar "/Users/johnmcnamara/Downloads/PathImageSegmentation-data/training/images"
java -jar build/libs/PathImageSegmentation-TCO-17-1.0-SNAPSHOT.jar "/Users/johnmcnamara/Downloads/PathImageSegmentation-data/testing/images"