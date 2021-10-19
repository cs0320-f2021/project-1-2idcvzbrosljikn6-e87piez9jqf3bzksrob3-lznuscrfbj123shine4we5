For a developer to add a new command to the REPL, he or she should decide what
the input to the REPL will be and then create a new helper method in our Main
class which executes the desired functionality, optionally passing in a string
array of arguments. Then the developer should add a new line to our createHashMap
method with the key being a String with the name of the command and the value
either the name of the helper method in the case where we need extra arguments,
or (String[] args) -> method call when we don't. The latter case works around
the interface requirements of methods which don't require extra arguments, so we
just pass a String array anyway but don't use it in the method call.