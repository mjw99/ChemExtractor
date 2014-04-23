# Chemextractor

A mini proof of concept example that uses OSCAR4 to extract chemical terms from a PDF and return them in an ad-hoc JSON format. Currently, it will return the Chemicals in InChI and InChIKeys format. These can be resolved at the RSC resolver [here](http://www.chemspider.com/inchi-resolver/Resolver.aspx).

# Quick Start

This will compile and install the command line tool (oscarpdf2jsoncli) to the Debian based OS.

## [Debian Wheezy](http://www.debian.org/releases/wheezy/) / [Ubuntu Precise](http://releases.ubuntu.com/precise/)

    $ sudo apt-get install git maven openjdk-7-jdk
    $ git clone https://bitbucket.org/mjw99/chemextractor.git
    $ cd chemextractor
    $ mvn clean package ; sudo dpkg -i ./target/*.deb
    $
    $ oscarpdf2json -i foo.pdf