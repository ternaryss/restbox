# restbox make file.
#
# @author TSS

app = restbox
checksum = checksum.md5
db = data
gradle = ./gradlew
launcher_unix = launcher.sh
launcher_win = launcher.bat
launchers_path = launchers
path = build/libs
profile = --spring.profiles.active
version = $(shell $(gradle) properties --no-daemon --console=plain -q | grep "^version:" | awk '{print $$2}')

all: build

build: clean
	echo "--- Building $(app)-$(version) ---"
	$(gradle) build

clean:
	echo "--- Cleaning $(app)-$(version) ---"
	$(gradle) clean
	rm -rf $(db)
	rm -rf $(checksum)
	rm -rf $(app)-$(version)
	rm -rf "$(app)-$(version).zip"

release: build
	echo "--- Making relase of $(app)-$(version) ---"
	mkdir $(app)-$(version)
	cp $(path)/$(app)-$(version).jar $(app)-$(version)/
	cp $(launchers_path)/$(launcher_unix) $(app)-$(version)/
	cp $(launchers_path)/$(launcher_win) $(app)-$(version)/
	sed -i "s/app=.*/app='$(app)'/" $(app)-$(version)/$(launcher_unix)
	sed -i "s/version=.*/version='$(version)'/" $(app)-$(version)/$(launcher_unix)
	sed -i "s/db=.*/db='$(db)'/" $(app)-$(version)/$(launcher_unix)
	sed -i "s/set app=.*/set app=$(app)/" $(app)-$(version)/$(launcher_win)
	sed -i "s/set version=.*/set version=$(version)/" $(app)-$(version)/$(launcher_win)
	sed -i "s/set db=.*/set db=$(db)/" $(app)-$(version)/$(launcher_win)
	cd $(app)-$(version) && md5sum $(app)-$(version).jar > $(checksum)
	cp $(app)-$(version)/$(checksum) $(checksum)
	rm -rf $(app)-$(version)/$(checksum)
	zip "$(app)-$(version).zip" -r $(app)-$(version)
	rm -rf $(app)-$(version)

run: build
	java -jar $(path)/$(app)-$(version).jar

run-invalid: build
	java -jar $(path)/$(app)-$(version).jar $(profile)=invalid

run-valid: build
	java -jar $(path)/$(app)-$(version).jar $(profile)=valid

test:
	echo "--- Testing $(app)-$(version) ---"
	$(gradle) cleanTest test
