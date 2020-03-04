#!/usr/bin/env bash

#===============================================================================
#
#          FILE: build.sh
#
#         USAGE: build.sh
#                This script must be run from top project folder, i.e folder
#                where the \src\ and \lib\ subfolders exist.
#                You need to "chmod +x build.sh" before you can run it
#
#   DESCRIPTION: Linux/Mac build script for the WAFFLES CS126 coursework.
#
#       OPTIONS: [-h, -b, -c, -d, -j, -r, -t, -z], see -h for more details
#
#  REQUIREMENTS: java, javac, jar
#
#       AUTHORS: Ian Tu, i.tu@warwick.ac.uk
#                James Van Hinsbergh, j.van-hinsbergh@warwick.ac.uk
#
#  ORGANIZATION: University of Warwick
#
#       CREATED: 01/01/20 00:00:00
#
#      REVISION: 1.2.6
#
#===============================================================================

function run () {
    PORTCHECK=$(netstat -antp 2>/dev/null | grep $PORTNO | grep LISTEN)

    if [[ "${PORTCHECK:-0}" == 0 ]]
    then
        :
    else
        echo
        echo "  [ERROR]    Port $PORTNO is in use." 
        echo "             Please close the application that is using it or try another port."
        echo
        exit 1
    fi

    rm -rf "$WORK_DIR/target/BOOT-INF"
    rm -rf "$WORK_DIR/target/*.tmp"
    rm -rf "$WORK_DIR/target/*.jar"

    mkdir -p "$WORK_DIR/target/BOOT-INF/classes/"

    echo
    echo "[SUCCESS]    Directories set up."
    echo
    echo "   [INFO]    Compiling your files..."
    echo

    if [ ! -d "$WORK_DIR/lib" ]
    then
        echo
        echo "  [ERROR]   No /lib folder, now exiting..."
        echo
        exit 1
    fi

    if [ ! -f "$WORK_DIR/waffles.jar" ]
    then
        echo
        echo "  [ERROR]   No waffles.jar, now exiting..."
        echo
        exit 1
    fi

    javac -d "$WORK_DIR/target/BOOT-INF/classes/" -encoding "UTF-8" -cp "$WORK_DIR/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/src/main/java/uk/ac/warwick/cs126/stores/CustomerStore.java"
    if [ ! -f "$WORK_DIR/target/BOOT-INF/classes/uk/ac/warwick/cs126/stores/CustomerStore.class" ]
    then
        echo
        echo "  [ERROR]    CustomerStore.java hasn't compiled, exiting..."
        echo
        exit 1
    fi        
    
    javac -d "$WORK_DIR/target/BOOT-INF/classes/" -encoding "UTF-8" -cp "$WORK_DIR/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/src/main/java/uk/ac/warwick/cs126/stores/FavouriteStore.java"
    if [ ! -f "$WORK_DIR/target/BOOT-INF/classes/uk/ac/warwick/cs126/stores/FavouriteStore.class" ]
    then
        echo
        echo "  [ERROR]    FavouriteStore.java hasn't compiled, exiting..."
        echo
        exit 1
    fi    

    javac -d "$WORK_DIR/target/BOOT-INF/classes/" -encoding "UTF-8" -cp "$WORK_DIR/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/src/main/java/uk/ac/warwick/cs126/stores/RestaurantStore.java"
    if [ ! -f "$WORK_DIR/target/BOOT-INF/classes/uk/ac/warwick/cs126/stores/RestaurantStore.class" ]
    then
        echo
        echo "  [ERROR]    RestaurantStore.java hasn't compiled, exiting..."
        echo
        exit 1
    fi

    javac -d "$WORK_DIR/target/BOOT-INF/classes/" -encoding "UTF-8" -cp "$WORK_DIR/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/src/main/java/uk/ac/warwick/cs126/stores/ReviewStore.java"
    if [ ! -f "$WORK_DIR/target/BOOT-INF/classes/uk/ac/warwick/cs126/stores/ReviewStore.class" ]
    then
        echo
        echo "  [ERROR]    ReviewStore.java hasn't compiled, exiting..."
        echo
        exit 1
    fi
    
    echo "[SUCCESS]    Files compiled."

    cp waffles.jar "$WORK_DIR/target/waffles-current-build-port-$PORTNO.jar"

    jar uf "$WORK_DIR/target/waffles-current-build-port-$PORTNO.jar" -C "$WORK_DIR/target/" "BOOT-INF"

    java -Xmx1700m -jar "$WORK_DIR/target/waffles-current-build-port-$PORTNO.jar" --server.port=$PORTNO

    echo
    echo "Run complete."

}

function clean () {
    echo
    echo "   [INFO]   Cleaning build directories..."
    rm -rf "$WORK_DIR/target"
    echo
    echo "[SUCCESS]   Clean complete."
    echo
}

function data () {
    echo
    echo "   [INFO]   Copying data files..."
    jar xf "$WORK_DIR/waffles.jar" "BOOT-INF/classes/data/placeData.tsv" "BOOT-INF/classes/data/favouriteData.csv" "BOOT-INF/classes/data/reviewData.tsv" "BOOT-INF/classes/data/customerData.csv" "BOOT-INF/classes/data/restaurantData.csv"
    rm -rf "$WORK_DIR/data/full"
    mkdir -p "$WORK_DIR/data/full"
    mv -fT "$WORK_DIR/BOOT-INF/classes/data/" "$WORK_DIR/data/full/"
    rm -rf "$WORK_DIR/BOOT-INF"
    if [ ! -f "$WORK_DIR/data/full/placeData.tsv" ]
    then
        echo
        echo "  [ERROR]    Copying data files unsuccessful. Exiting..."
        echo
        exit 1
    fi
    if [ ! -f "$WORK_DIR/data/full/favouriteData.csv" ]
    then
        echo
        echo "  [ERROR]    Copying data files unsuccessful. Exiting..."
        echo
        exit 1
    fi
    if [ ! -f "$WORK_DIR/data/full/reviewData.tsv" ]
    then
        echo
        echo "  [ERROR]    Copying data files unsuccessful. Exiting..."
        echo
        exit 1
    fi
    if [ ! -f "$WORK_DIR/data/full/customerData.csv" ]
    then
        echo
        echo "  [ERROR]    Copying data files unsuccessful. Exiting..."
        echo
        exit 1
    fi
    if [ ! -f "$WORK_DIR/data/full/restaurantData.csv" ]
    then
        echo
        echo "  [ERROR]    Copying data files unsuccessful. Exiting..."
        echo
        exit 1
    fi
    echo
    echo "[SUCCESS]   Successfully copied full data files into /data/full folder."
    echo
}

function zip () {
    echo
    echo "   [INFO]   Processing submission..."
    rm -rf "$WORK_DIR/submission.zip"
    rm -rf "$WORK_DIR/target/$SUBMISSION_FOLDER"
    rm -rf "$WORK_DIR/target/classes"
    echo
    echo "   [INFO]   Copying files..."
    mkdir -p "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/stores"
    cp -afT "$WORK_DIR/src/main/java/uk/ac/warwick/cs126/stores" "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/stores"
    mkdir -p "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/structures"
    cp -afT "$WORK_DIR/src/main/java/uk/ac/warwick/cs126/structures" "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/structures"
    mkdir -p "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/util"
    cp -afT "$WORK_DIR/src/main/java/uk/ac/warwick/cs126/util" "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/util"

    if [ ! -f "$WORK_DIR/Report.md" ]
    then
        echo
        echo "  [ERROR]   Report.md does not exist, now exiting..."
        echo
        exit 1
    else 
        echo
        echo "[SUCCESS]   Exists: Report.md"
    fi
    cp -af "$WORK_DIR/Report.md" "$WORK_DIR/target/$SUBMISSION_FOLDER/"
    echo 
    echo "   [INFO]   Testing files..."
    
    if [ ! -d "$WORK_DIR/lib" ]
    then
        echo
        echo "  [ERROR]   No /lib folder, now exiting..."
        echo
        exit 1
    fi

    echo
    mkdir -p "$WORK_DIR/target/classes"

    if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/util/ConvertToPlace.class" ]
    then
        javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/util/ConvertToPlace.java"
        if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/util/ConvertToPlace.class" ]
        then
            echo
            echo "  [ERROR]   ConvertToPlace.java hasn't compiled, now exiting..."
            echo
            exit 1
        else 
            echo "[SUCCESS]   Compiled successfully: ConvertToPlace.java"
        fi
    else 
        echo "[SUCCESS]   Compiled successfully: ConvertToPlace.java"
    fi

    if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/util/HaversineDistanceCalculator.class" ]
    then
        javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/util/HaversineDistanceCalculator.java"
        if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/util/HaversineDistanceCalculator.class" ]
        then
            echo
            echo "  [ERROR]   HaversineDistanceCalculator.java hasn't compiled, now exiting..."
            echo
            exit 1
        else 
            echo "[SUCCESS]   Compiled successfully: HaversineDistanceCalculator.java"
        fi
    else 
        echo "[SUCCESS]   Compiled successfully: HaversineDistanceCalculator.java"
    fi

    if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/util/DataChecker.class" ]
    then
        javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/util/DataChecker.java"
        if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/util/DataChecker.class" ]
        then
            echo
            echo "  [ERROR]   DataChecker.java hasn't compiled, now exiting..."
            echo
            exit 1
        else 
            echo "[SUCCESS]   Compiled successfully: DataChecker.java"
        fi
    else 
        echo "[SUCCESS]   Compiled successfully: DataChecker.java"
    fi

    if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/util/KeywordChecker.class" ]
    then
        javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/util/KeywordChecker.java"
        if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/util/KeywordChecker.class" ]
        then
            echo
            echo "  [ERROR]   KeywordChecker.java hasn't compiled, now exiting..."
            echo
            exit 1
        else 
            echo "[SUCCESS]   Compiled successfully: KeywordChecker.java"
        fi
    else 
        echo "[SUCCESS]   Compiled successfully: KeywordChecker.java"
    fi

    if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/util/StringFormatter.class" ]
    then
        javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/util/StringFormatter.java"
        if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/util/StringFormatter.class" ]
        then
            echo
            echo "  [ERROR]   StringFormatter.java hasn't compiled, now exiting..."
            echo
            exit 1
        else 
            echo "[SUCCESS]   Compiled successfully: StringFormatter.java"
        fi
    else 
        echo "[SUCCESS]   Compiled successfully: StringFormatter.java"
    fi

    if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/stores/CustomerStore.class" ]
    then
        javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/stores/CustomerStore.java"
        if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/stores/CustomerStore.class" ]
        then
            echo
            echo "  [ERROR]   CustomerStore.java hasn't compiled, now exiting..."
            echo
            exit 1
        else 
            echo "[SUCCESS]   Compiled successfully: CustomerStore.java"
        fi
    else 
        echo "[SUCCESS]   Compiled successfully: CustomerStore.java"
    fi

    if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/stores/FavouriteStore.class" ]
    then
        javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/stores/FavouriteStore.java"
        if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/stores/FavouriteStore.class" ]
        then
            echo
            echo "  [ERROR]   FavouriteStore.java hasn't compiled, now exiting..."
            echo
            exit 1
        else 
            echo "[SUCCESS]   Compiled successfully: FavouriteStore.java"
        fi
    else 
        echo "[SUCCESS]   Compiled successfully: FavouriteStore.java"
    fi

    if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/stores/RestaurantStore.class" ]
    then
        javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/stores/RestaurantStore.java"
        if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/stores/RestaurantStore.class" ]
        then
            echo
            echo "  [ERROR]   RestaurantStore.java hasn't compiled, now exiting..."
            echo
            exit 1
        else 
            echo "[SUCCESS]   Compiled successfully: RestaurantStore.java"
        fi
    else 
        echo "[SUCCESS]   Compiled successfully: RestaurantStore.java"
    fi

    if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/stores/ReviewStore.class" ]
    then
        javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/target/$SUBMISSION_FOLDER/src/main/java/uk/ac/warwick/cs126/stores/ReviewStore.java"
        if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/stores/ReviewStore.class" ]
        then
            echo
            echo "  [ERROR]   ReviewStore.java hasn't compiled, now exiting..."
            echo
            exit 1
        else 
            echo "[SUCCESS]   Compiled successfully: ReviewStore.java"
        fi
    else 
        echo "[SUCCESS]   Compiled successfully: ReviewStore.java"
    fi
    
    echo
    echo "   [INFO]   Zipping files..."
    jar cvfM "$WORK_DIR/submission.zip" -C "$WORK_DIR/target" "$SUBMISSION_FOLDER" > /dev/null
    echo
    echo "[SUCCESS]   Successfully zipped submission: submission.zip"
    echo
}

function help () {
    echo  "
    Usage: ${0##*/} [-h,-b,-c,-d,-j,-r,-t,-z]

        -h  display this help and exit
        -b  compile sources
        -c  delete /target folder
        -j  run the main class of given input
        -d  copy full and original data to /target folder
        -r  compile sources and run WAFFFLES website
        -t  compile uk\ac\warwick\cs126\test\TestRunner.java and run its main class
        -z  zip up files for submission
       
    To specify the port number of website:    
        -r 9090     Run the WAFFLES website on port 9090, replace 9090 with your choice of port

    Examples to run in terminal:
        ./build.sh -r    Will compile current sources and run the WAFFLES website on port 8080
        ./build.sh -t    Will compile TestRunner and run its main class
        ./build.sh -b    Will compile sources

    This will compile sources and try to run the WAFFLES website on port 9090:
        ./build.sh -r 9090    

    This will run the main class of TestRunner.java, it must be compiled beforehand before running:
        ./build.sh -j uk.ac.warwick.cs126.test.TestRunner    
"
}

function test () {
    echo
    echo "   [INFO]   Setting up temporary directories..."
    echo

    rm -rf "$WORK_DIR/target/classes"

    mkdir -p "$WORK_DIR/target/classes"

    echo "[SUCCESS]   Directories set up."
    echo
    echo "   [INFO]   Compiling your files..."
    echo

    if [ ! -d "$WORK_DIR/lib" ]
    then
        echo
        echo "  [ERROR]   No /lib folder, now exiting..."
        echo
        exit 1
    fi

    javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/src/main/java/uk/ac/warwick/cs126/test/TestRunner.java"

    if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/test/TestRunner.class" ]
    then
        echo
        echo "  [ERROR]   TestRunner.java hasn't compiled, now exiting..."
        echo
        exit 1
    fi

    echo "[SUCCESS]   Files compiled."
    echo
    echo "   [INFO]   Running tests ..."
    echo
    echo
    java -cp "$WORK_DIR/target/classes/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" uk.ac.warwick.cs126.test.TestRunner
    echo
    echo 
    echo "[SUCCESS]   Tests complete."
    echo
}

function compile () {
    if [ ! -d "$WORK_DIR/lib" ]
    then
        echo
        echo "  [ERROR]   No /lib folder, now exiting..."
        echo
        exit 1
    fi

    echo
    echo "   [INFO]   Setting up temporary directories..."
    echo

    rm -rf "$WORK_DIR/target/classes"

    mkdir -p "$WORK_DIR/target/classes"

    echo "[SUCCESS]   Directories set up."
    echo
    echo "   [INFO]   Compiling your files..."
    echo

    for f in "$WORK_DIR"/src/main/java/uk/ac/warwick/cs126/test/*.java
    do
        FILENAME_WITH_EXT=${f##*/}
        FILENAME=${FILENAME_WITH_EXT%.*}
        if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/test/$FILENAME.class" ]
        then
            javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/src/main/java/uk/ac/warwick/cs126/test/$FILENAME.java"
            if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/test/$FILENAME.class" ]
            then
                echo
                echo "  [ERROR]   $FILENAME_WITH_EXT hasn't compiled, now exiting..."
                echo
                exit 1
            fi
        fi
    done

    for f in "$WORK_DIR"/src/main/java/uk/ac/warwick/cs126/stores/*.java
    do
        FILENAME_WITH_EXT=${f##*/}
        FILENAME=${FILENAME_WITH_EXT%.*}
        if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/stores/$FILENAME.class" ]
        then
            javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/src/main/java/uk/ac/warwick/cs126/stores/$FILENAME.java"
            if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/stores/$FILENAME.class" ]
            then
                echo
                echo "  [ERROR]   $FILENAME_WITH_EXT hasn't compiled, now exiting..."
                echo
                exit 1
            fi
        fi
    done

    for f in "$WORK_DIR"/src/main/java/uk/ac/warwick/cs126/util/*.java
    do
        FILENAME_WITH_EXT=${f##*/}
        FILENAME=${FILENAME_WITH_EXT%.*}
        if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/util/$FILENAME.class" ]
        then
            javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/src/main/java/uk/ac/warwick/cs126/util/$FILENAME.java"
            if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/util/$FILENAME.class" ]
            then
                echo
                echo "  [ERROR]   $FILENAME_WITH_EXT hasn't compiled, now exiting..."
                echo
                exit 1
            fi
        fi
    done

    for f in "$WORK_DIR"/src/main/java/uk/ac/warwick/cs126/structures/*.java
    do
        FILENAME_WITH_EXT=${f##*/}
        FILENAME=${FILENAME_WITH_EXT%.*}
        if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/structures/$FILENAME.class" ]
        then
            javac -d "$WORK_DIR/target/classes/" -encoding "UTF-8" -cp "$WORK_DIR/src/main/java/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$WORK_DIR/src/main/java/uk/ac/warwick/cs126/structures/$FILENAME.java"
            if [ ! -f "$WORK_DIR/target/classes/uk/ac/warwick/cs126/structures/$FILENAME.class" ]
            then
                echo
                echo "  [ERROR]   $FILENAME_WITH_EXT hasn't compiled, now exiting..."
                echo
                exit 1
            fi
        fi
    done

    echo "[SUCCESS]   Files compiled."
    echo
}

function runMain () {
    if [ ! -d "$WORK_DIR/lib" ]
    then
        echo
        echo "  [ERROR]   No /lib folder, now exiting..."
        echo
        exit 1
    fi

    if [ ! -d "$WORK_DIR/target/classes" ]
    then
        echo
        echo "  [ERROR]   No /target/classes folder, now exiting..."
        echo
        exit 1
    fi

    if [ -z $MAINCLASS ]
    then
        echo
        echo "  [ERROR]   No argument given."
        echo "            Should be of the form: uk.ac.warwick.cs126.test.TestRunner"
        echo "            Now exiting..."
        echo 
        exit 1
    fi

    echo
    echo "   [INFO]   Running main class of $MAINCLASS ..."
    echo

    java -cp "$WORK_DIR/target/classes/:$WORK_DIR/lib/commons-io-2.6.jar:$WORK_DIR/lib/cs126-interfaces-1.2.6.jar:$WORK_DIR/lib/cs126-models-1.2.6.jar:" "$MAINCLASS"

    echo
}

CURRENT_DIR=$(pwd)
SCRIPT_FOLDER=$(dirname "$0")
cd $SCRIPT_FOLDER

WORK_DIR=$(pwd)
SUBMISSION_FOLDER="cs126-waffles-coursework"

if [ -z $2 ]
then
    PORTNO=8080
    MAINCLASS=
else
    PORTNO=$2
    MAINCLASS=$2
fi

# parse cmd options
while getopts :hbcdjrtz opt; do 
    case $opt in
        h)
            help
            cd "$CURRENT_DIR"
            exit 0
            ;;
        b)
            compile
            cd "$CURRENT_DIR"
            exit 0
            ;;
        c)
            clean
            cd "$CURRENT_DIR"
            exit 0
            ;;
        d)
            data
            cd "$CURRENT_DIR"
            exit 0
            ;;
        j)
            runMain
            cd "$CURRENT_DIR"
            exit 0
            ;;
        r)
            run
            cd "$CURRENT_DIR"
            exit 0
            ;;
        t)
            test
            cd "$CURRENT_DIR"
            exit 0
            ;;
        z)
            zip
            cd "$CURRENT_DIR"
            exit 0
            ;;
        \?)
            echo
            echo "    Invalid option given."
            help
            cd "$CURRENT_DIR"
            exit 0
            ;;
    esac
done

shift "$((OPTIND-1))"   # Discard the options
if [ $OPTIND -eq 1 ]; then 
    echo
    echo "    No option given."
    help
fi # no options specified
