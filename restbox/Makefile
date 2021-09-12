# restbox make file.
#
# @author TSS

app = restbox
gradle = ./gradlew
path = build/libs
profile = --spring.profiles.active
version = 0.0.0

all: build

build: clean
	echo "--- Building $(app)-$(version) ---"
	$(gradle) build

clean:
	echo "--- Cleaning $(app)-$(version) ---"
	$(gradle) clean

run: build
	java -jar $(path)/$(app)-$(version).jar

run-invalid: build
	java -jar $(path)/$(app)-$(version).jar $(profile)=invalid

run-valid: build
	java -jar $(path)/$(app)-$(version).jar $(profile)=valid

test:
	echo "--- Testing $(app)-$(version) ---"
	$(gradle) cleanTest test
