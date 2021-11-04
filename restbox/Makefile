# restbox make file.
#
# @author TSS

app = restbox
db = data/
gradle = ./gradlew
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

run: build
	java -jar $(path)/$(app)-$(version).jar

run-invalid: build
	java -jar $(path)/$(app)-$(version).jar $(profile)=invalid

run-valid: build
	java -jar $(path)/$(app)-$(version).jar $(profile)=valid

test:
	echo "--- Testing $(app)-$(version) ---"
	$(gradle) cleanTest test
