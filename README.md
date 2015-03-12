clj-thrust Notepad (Example)
============================

This is a very basic sample project which shows how one might go about using [clj-thrust](https://github.com/solicode/clj-thrust) to create a simple Notepad-like application.

This project exists simply for learning purposes. It's not meant to be anything more than that. It also has comments in the source explaining how some things work.

Overview
--------

This project is written with Clojure (for the sever) and ClojureScript (for the client). When you run the app, a server is started up at port 7171 and the [Thrust](https://github.com/breach/thrust) shell connects to it to display the app in its own window.

There is actually very little code dependency on Thrust itself. We're merely using it as a container so that we can run our simple Notepad web app as a desktop application. You can easily swap out this container for something else. Anything that can act as a browser will do. For example, JavaFX's WebView or SWT's Browser are a couple of other options on the JVM. [Middlebrow](https://github.com/solicode/middlebrow) is another project which takes this one step further and abstracts library-specific code out for you so that switching between various containers is even easier.

Getting Started
---------------

### Running the app

Simply clone this repository and run it with [Leiningen](http://leiningen.org):

```
lein run
```

This will open the Thrust shell which is hosting our web app. You can also open it directly in your browser (any modern browser should do) by going to http://localhost:7171 while the server is running.

### Development

The `project.clj` file has [cljsbuild](https://github.com/emezeske/lein-cljsbuild), [figwheel](https://github.com/bhauman/lein-figwheel), and [weasel](https://github.com/tomjakubowski/weasel) already set up for you. How you want to set up your workflow is entirely up to you though. The most basic and easiest way to get up and running would probably be to just run cljsbuild with automatic incremental compilation turned on, like so:

```
lein cljsbuild auto dev
```

So any changes you make to your ClojureScript files will automatically be compiled for you upon saving. After compilation, simply refresh your app to see your changes.

Also, feel free to use the browser when you're developing instead of using the Thrust window. Either will do, but some people may prefer the browser because they have certain add-ons/extensions that they find indispensable when developing web apps.

#### Figwheel

Figwheel is nice in that it enables live code reloading, meaning you do not have to refresh the page to see your new changes. For more details on what Figwheel is capable of, check out the [project page here](https://github.com/bhauman/lein-figwheel).

To get up and running with Figwheel, run the following command:

```
lein figwheel
```

This should have started up the Figwheel server, so now when you open your web app (in Thrust or the web browser), it should be watching for changes in your source code. Once you save a file, those changes will be hot loaded and you should see them immediately! Feels great, doesn't it? (^^) I personally do most of my work this way, but go ahead and use the method that you feel is most productive for you.

#### Browser REPL

Getting the Browser REPL up and running is a little more involved, but it's still not too bad.

1.	Start a REPL. This will be your Clojure REPL (for the server code). Then run:

	```clojure
	(start-server)
	```

2.	Start another REPL. This will be your browser REPL (for the client code). Then run the following:

	```clojure
	(require 'weasel.repl.websocket)

	(cemerick.piggieback/cljs-repl
	  :repl-env (weasel.repl.websocket/repl-env
	              :ip "0.0.0.0" :port 9001))
	```

	This REPL should now be switched to ClojureScript.

3.	Open your web app. Either in the browser at http://localhost:7171, or through Thrust, by running the following in your Clojure REPL (the first one we started):

	```
	(open-window)
	```

	And you should be all set. Switch to your browser REPL and start issuing commands! Or go back to your Clojure REPL when you want to interact with your server code.

License
-------

Copyright © 2015 Solicode

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
