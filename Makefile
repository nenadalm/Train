BUILD_DIR=build
BUILD_DIR_TRAIN=${BUILD_DIR}/Train
BAT_EXECUTABLE=${BUILD_DIR}/Train/start.bat
BASH_EXECUTABLE=${BUILD_DIR}/Train/start.sh

clean:
	rm -rf "${BUILD_DIR}"

build: clean
	mvn package
	mkdir -p "${BUILD_DIR_TRAIN}"
	find target/ -name '*-jar-with-dependencies.jar' | xargs -I{} cp {} "${BUILD_DIR_TRAIN}/Train.jar"
	rsync -r --exclude=save content/ "${BUILD_DIR_TRAIN}/content"
	cp -R target/natives "${BUILD_DIR_TRAIN}"

	echo 'java -Djava.library.path="%~dp0/lib" -jar Train.jar' > "${BAT_EXECUTABLE}"
	chmod +x "${BAT_EXECUTABLE}"

	echo '#!/bin/bash' > "${BASH_EXECUTABLE}"
	echo 'java -Djava.library.path="$$(dirname $$0)/natives" -jar Train.jar' >> "${BASH_EXECUTABLE}"
	chmod +x "${BASH_EXECUTABLE}"

	tar -cJf "${BUILD_DIR}/Train.tar.xz" "${BUILD_DIR_TRAIN}"
	zip -r "${BUILD_DIR}/Train.zip" "${BUILD_DIR_TRAIN}"
