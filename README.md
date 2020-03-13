[![Build Status](https://travis-ci.org/Blazebit/blaze-storage.svg?branch=master)](https://travis-ci.org/Blazebit/blaze-storage)

blaze-storage
==========
blaze-storage is an abstraction over various storage models and offers transparent access to files

What is it?
===========

blaze-storage is an abstraction for storage APIs like S3, local filesystem etc.
It provides enables transparent access to remote files and is easily extendible for additional protocols.

How to use it?
==============
Currently you have to build it yourself, but soon we will publish the version 1.0 to
the Maven Central.

Running Arquillian tests in IntelliJ
====================================
To be able to run the tests in IntelliJ you must configure a "Manual container configuration" Run Configuration
with the "Working directory" `$MODULE_WORKING_DIR$`. 
 
Licensing
=========

This distribution, as a whole, is licensed under the terms of the Apache
License, Version 2.0 (see LICENSE.txt).

References
==========

Project Site:              http://blazebit.com/storage (Coming soon)
