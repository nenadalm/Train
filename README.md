[![Build Status](https://secure.travis-ci.org/nenadalm/Train.png?branch=master)](http://travis-ci.org/nenadalm/Train)

Train
=====

Remake of  old DOS game [Vlak][3]

(still in development)

Used technologies
-----------------
[JavaSE][1], [Slick2D][2], [Maven][4], [PicoContainer][5]

Setup project
-------------
* setup [Maven][4]

* install dependencies

    ```$ mvn dependency:copy-dependencies```

* setup Eclipse (this will automatically configure your build path)

    ```$ mvn eclipse:eclipse```

* format code

    ```$ mvn formatter:format```

Create distributable package
----------------------------

* you can create directory with all necessary files by

    ```$ mvn package```

* it'll put all necessary files for run into build/

Show outdated dependencies
--------------------------

```$ mvn versions:display-dependency-updates```

BSD License
-----------

Copyright (c) 2012, Train

All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

* Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
* Neither the name of the Train nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

[1]: http://www.oracle.com/technetwork/java/javase/overview/index.html
[2]: http://slick.cokeandcode.com/
[3]: http://www.bestoldgames.net/eng/old-games/vlak.php
[4]: http://maven.apache.org/
[5]: http://picocontainer.codehaus.org/
