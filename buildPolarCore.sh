mkdir libs
git clone https://github.com/saltedfishclub/PolarCore
cd PolarCore
chmod +x ./gradlew
./gradlew shadowjar
mv ./build/libs/*.jar ../libs
cd ..