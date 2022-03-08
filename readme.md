# Chemextractor

A mini proof of concept example that uses [OSCAR4](https://github.com/BlueObelisk/oscar4/) to extract chemical terms from a PDF and return them in an ad-hoc JSON format. Currently, it will return the Chemicals in standard [InChI](http://en.wikipedia.org/wiki/International_Chemical_Identifier) and standard [InChIKeys](http://en.wikipedia.org/wiki/International_Chemical_Identifier#InChIKey) format. These can be resolved with the [RSC ChemSpider](http://www.chemspider.com/) tool.

# Quick Start

This will compile and install the command line tool (oscarpdf2jsoncli) to the Debian based OS.

## [Debian Wheezy](http://www.debian.org/releases/wheezy/) / [Ubuntu Precise](http://releases.ubuntu.com/precise/)

    $ sudo apt-get install git maven openjdk-8-jdk
    $ git clone https://github.com/mjw99/ChemExtractor
    $ cd ChemExtractor ; mvn clean package ; sudo dpkg -i ./target/*.deb
    $
    $ oscarpdf2json foo.pdf

# Examples

## Extract chemical data from a single pdf

    $ cd /tmp
    $ wget http://patentimages.storage.googleapis.com/pdfs/US20110000125.pdf
    $ oscarpdf2json US20110000125.pdf > US20110000125.json

## Extract chemical data from multiple pdfs
   
    $ cd src/test/resources/name/mjw/chemextractor/chem_sample_patents
    $ oscarpdf2json *.pdf > all.json
