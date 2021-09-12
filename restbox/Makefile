# restbox make file.
#
# @author TSS

app = restbox
gradle = ./gradlew
path = build/libs
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

test:
	echo "--- Testing $(app)-$(version) ---"
	$(gradle) cleanTest test
