# Note: Anywhere you see @, that means I want to prevent that
#     command from printing (and only want to see the result of
#     executing that command)

# Defining some variables to prevent typos
COMPILE_DIR         = target
SRC_DIR             = .
SOURCE_FILE         = sources

# clean should be used to remove all compiled (.class) files
# the find command on *nix finds all files (-type f) starting from the
#    current directory (.), where the filename ends in ".class", and
#    deletes (-delete) them
clean:
	@echo "Deleting all class files..."
	@find . -name "*.class" -type f -delete
	@echo "Deleting compiled files..."
	@rm -rf $(COMPILE_DIR)
	@rm -f $(SOURCE_FILE)
	@rm -f $(TARBALL)

# compile depends on clean to ensure we start from a clean slate
#     every time, so that any changes we make to our java files are
#     re-compiled
# the find command then looks for all files recursively from the
#     src directory (./src), with the extension ".java"
# this file listing is then output into a file named "sources.txt",
#     which is used as an input to the java compiler (javac)
# the javac command then compiles the files listed in "sources.txt"
#     into a directory (-d) named "target" (just to keep the .class
#     files out of our nicely organized packages)
# finally, we remove the temporary "sources.txt" file
all: clean
	@echo "Compiling..."
	@find $(SRC_DIR) -name "*.java" -not -path "./tests/*" > $(SOURCE_FILE)
	@javac -d $(COMPILE_DIR) @$(SOURCE_FILE)
	@rm $(SOURCE_FILE)

JUNIT_JAR         = lib/junit-platform-console-standalone-1.9.3.jar

# Pretty much the same as compile, but we want to include the tests
#     and their dependencies (the junit jar)
# find adds all files with the ".java" extension from the current
#     directory (instead of the ./src directory) to the "sources.txt"
#     file. could I have used the compile directive? sure, but I'm
#     lazy, and this was easier for me
# since there are some additional dependencies, we need to set the
#     classpath (-cp) for the java compiler to include those dependencies
# then we clean up the temporary "sources.txt" file, as before
all-tests: clean
	@echo "Compiling for testing..."
	@find . -name "*.java" > $(SOURCE_FILE)
	@javac -d $(COMPILE_DIR) -cp $(COMPILE_DIR):$(JUNIT_JAR):. @$(SOURCE_FILE)
	@rm $(SOURCE_FILE)

# Depends on all-tests to get the tests _and_ the src directory compiled
# When we run, we want to run the junit jar in order to run the tests,
#     but we need to tell java that the classpath that should be used
#     is the target directory. --scan-classpath just looks in the classpath
#     for _any_ tests, and executes those
test-all: all-tests tools
	@echo "Running tests..."
	@java -jar $(JUNIT_JAR) -cp $(COMPILE_DIR) --disable-banner --include-classname=.* --scan-classpath

# we can run individual tests by specifying them using the --select-class flag
# the TEST_NAME environment variable must be the fully qualified class name,
#     like tests.packageName.TestClassName
# to run a single test, us:
#     make test TEST_NAME=tests.packageName.TestClassName
#     make test-method METHOD_NAME=tests.packageName.TestClassName#methodName
test: all-tests
	@echo "Running $(TEST_NAME)..."
	@java -jar $(JUNIT_JAR) -cp $(COMPILE_DIR) -c $(TEST_NAME) --disable-banner

test-method: all-tests
	@echo "Running $(METHOD_NAME)..."
	@java -jar lib/junit-platform-console-standalone-1.9.3.jar -cp target -m $(METHOD_NAME)

tools: clean
	@javac -d target tools/CompilerTools.java
	@java -cp target tools.CompilerTools

assignment1: clean
	@javac -d target lexer/Lexer.java
	@java -cp target lexer.Lexer xsamples/new.x

assignment2: clean
	@javac -d target parser/Parser.java
	@java -cp target parser.Parser xsamples/new.x

assignment3: clean
	@javac -d target tools/AstRenderer.java
	@java -cp target tools.AstRenderer xsamples/new.x