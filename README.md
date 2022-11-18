# EinfProg-Test-Engine

A lot of examples in the test source folder.

## Usage Examples

- `Bsp01Test` class contains basic examples
- `Bsp01TestQuick` class contains examples using convenience methods
- `Bsp02Test` class contains examples using the builder methods (also for convenience)
- `Bsp01TestRegex` and `Bsp02TestRegex` classes contain examples using RegEx
- `Bsp03Test` class contains examples to check for methods and method signatures together with their output (no compiler error if they do not exist)

## Example Error Messages

Most examples can be found in the `Bsp01TestFail` class.

### Mismatched String

```-
Wrong console output!
==================================================
Expected: "? Dieselpreis pro Liter[Euro]: "
  (This is to be done:)
--------------------------------------------------
? Verbrauch 100km[l]: 
? Dieselpreis pro Liter[Euro]: 
                  ^
==================================================
Found: "? Dieselpreis pro tLiter[Euro]: "
  (This is your output:)
--------------------------------------------------
? Verbrauch 100km[l]: 
? Dieselpreis pro tLiter[Euro]: 
                  ^
==================================================
With the following console input:
--------------------------------------------------
2.0
3.0
4.0
5.0
==================================================
```

### Wrong Result Value

```-
Wrong console output!
==================================================
Expected: "6.0"
  (This is to be done:)
--------------------------------------------------
? Verbrauch 100km[l]: 
? Dieselpreis pro Liter[Euro]: 
Kosten pro 100km[Euro] = 6.0
                         ^
==================================================
Found: "6.3000300000000005"
  (This is your output:)
--------------------------------------------------
? Verbrauch 100km[l]: 
? Dieselpreis pro Liter[Euro]: 
Kosten pro 100km[Euro] = 6.3000300000000005
                         ^
==================================================
With the following console input:
--------------------------------------------------
2.0
3.0
4.0
5.0
==================================================
```

### Entire Output Line Missing

```-
Wrong console output!
==================================================
Expected: "Verhältnis S/D = "
  (This is to be done:)
--------------------------------------------------
? Verbrauch 100km[l]: 
? Dieselpreis pro Liter[Euro]: 
Kosten pro 100km[Euro] = 6.0
? Verbrauch 100km[kWh]: 
? Strompreis pro kWh[Euro]: 
Kosten pro 100km[Euro] = 20.0
Verhältnis S/D = 3.3333333333333335
^
==================================================
Found: ""
  (This is your output:)
--------------------------------------------------
? Verbrauch 100km[l]: 
? Dieselpreis pro Liter[Euro]: 
Kosten pro 100km[Euro] = 6.0
? Verbrauch 100km[kWh]: 
? Strompreis pro kWh[Euro]: 
Kosten pro 100km[Euro] = 20.0

^
==================================================
With the following console input:
--------------------------------------------------
2.0
3.0
4.0
5.0
==================================================
```

### Exception Thrown

```-
An error occurred while testing the program:
--------------------------------------------------
java.lang.RuntimeException: ERROR == 4! This exception gets thrown on purpose!
	at einfprog.Bsp01TestFail.testRun(Bsp01TestFail.java:40)
	at einfprog.Bsp01TestFail.lambda$testWithError$0(Bsp01TestFail.java:73)
	at einfprog.test_engine.OutputTest.run(OutputTest.java:18)
	at einfprog.test_engine.Engine.checkTest(Engine.java:94)
	at einfprog.Bsp01TestFail.testWithError(Bsp01TestFail.java:91)
	...
==================================================
```

### Wrong Method Signature

```-
Wrong signature of method "rollDieTest" in class "Bsp03TestFail":
==================================================
Expected: public
--------------------------------------------------
Found: private
==================================================
```

### Wrong Method Return Value

```-
Wrong return value when calling method "Bsp03.isMaexchen(1, 2)":
==================================================
Expected: false
--------------------------------------------------
Found: true
==================================================
```
