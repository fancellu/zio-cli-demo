## zio-cli-demp

A simple demo application to show zio-cli in action

### https://zio.dev/zio-http/

#### `sbt run`

```        _            ___ 
 ____  (_)___  _____/ (_)
/_  / / / __ \/ ___/ / / 
 / /_/ / /_/ / /__/ / /  
/___/_/\____/\___/_/_/   
                         


ziocli v0.1 -- a sample zio-cli app

USAGE

  $ ziocli <command>

COMMANDS

  - add                            Runs the add command, takes no params
  - addName [(-f, --flag)] <text>  Runs the addName command takes a name param and optional flag
  - subSub                         Runs the subsub command and takes subcommands one and two
  - addNumber <integer>            Runs the addNumber command takes an integer, must be 10 or less
  ```

#### `sbt "run add"`
```
Args=add
add
```

#### `sbt "run addName"`
```
Args=addName
Missing argument text.

Error: Missing argument text.
```

#### `sbt "run addName Dino --flag"`
```
Args=addName,Dino,--flag
addName Dino FLAG=true
```
#### `sbt "run addName Milo -f"`
```
Args=addName,Milo,-f
addName Milo FLAG=true
```
#### `sbt "run addName Neo"`
```
Args=addName,Neo
addName Neo FLAG=false
```

#### `sbt "run subsub"`
```
Args=subsub
        _            ___ 
 ____  (_)___  _____/ (_)
/_  / / / __ \/ ___/ / /
 / /_/ / /_/ / /__/ / /
/___/_/\____/\___/_/_/



ziocli v0.1 -- a sample zio-cli app

USAGE

  $ subSub <command>

DESCRIPTION

  Runs the subsub command and takes subcommands one and two

COMMANDS

  - one --name text        Add one subcommand description
  - two (-n, --name text)  Add two subcommand description

```

#### `sbt "run subsub one --name onename"`

```
Args=subsub,one,--name,onename
addName.one onename
```

#### `sbt "run subsub two -n twoname"`
```
Args=subsub,two,-n,twoname
addName.two twoname
```

#### `sbt "run addNumber 123"`
```
Args=addNumber,123
Error: 123 is too big, must be <=10
```

#### `sbt "run addNumber 10"`
```
Args=addNumber,10
addNumber 10
```