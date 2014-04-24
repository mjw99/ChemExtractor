# Chemextractor

A mini proof of concept example that uses [OSCAR4](https://bitbucket.org/wwmm/oscar4/wiki/Home) to extract chemical terms from a PDF and return them in an ad-hoc JSON format. Currently, it will return the Chemicals in [InChI](http://en.wikipedia.org/wiki/International_Chemical_Identifier) and [InChIKeys](http://en.wikipedia.org/wiki/International_Chemical_Identifier#InChIKey) format. These can be resolved at the RSC resolver [here](http://www.chemspider.com/inchi-resolver/Resolver.aspx).

# Quick Start

This will compile and install the command line tool (oscarpdf2jsoncli) to the Debian based OS.

## [Debian Wheezy](http://www.debian.org/releases/wheezy/) / [Ubuntu Precise](http://releases.ubuntu.com/precise/)

    $ sudo apt-get install git maven openjdk-7-jdk
    $ git clone https://bitbucket.org/mjw99/chemextractor.git
    $ cd chemextractor
    $ mvn clean package ; sudo dpkg -i ./target/*.deb
    $
    $ oscarpdf2json -i foo.pdf

# Examples

## Extract chemical data from a single pdf

    $ cd /tmp
    $ wget http://patentimages.storage.googleapis.com/pdfs/US20110000125.pdf
    $ oscarpdf2json -i US20110000125.pdf > US20110000125.json

## Extract chemical data from multiple pdfs
   
    $ cd src/test/resources/name/mjw/chemextractor/chem_sample_patents
    $ ls *.pdf | xargs -I{} sh -c "oscarpdf2json -i '{}' > '/tmp/{}.json'"
