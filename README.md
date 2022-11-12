# EinfProg-Test-Engine

A lot of examples in the test source folder.

## Example Error Messages

All examples can be found in the `Bsp01TestFail` class.

### Mismatched String

```-
org.opentest4j.AssertionFailedError: Wrong output detected!
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
With the following input:
--------------------------------------------------
2.0
3.0
4.0
5.0
==================================================
```

### Wrong Result Value

```-
org.opentest4j.AssertionFailedError: Wrong output detected!
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
With the following input:
--------------------------------------------------
2.0
3.0
4.0
5.0
==================================================
```

### Entire Output Line Missing

```-
org.opentest4j.AssertionFailedError: Wrong output detected!
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
Verhältnis S/D = 
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
With the following input:
--------------------------------------------------
2.0
3.0
4.0
5.0
==================================================
```

### Exception Thrown

```-
java.lang.RuntimeException: ERROR == 4
  at einfprog.Bsp01TestFail.testRun(Bsp01TestFail.java:43)
  at einfprog.test_engine.AbstractTest.run(AbstractTest.java:18)
  at einfprog.test_engine.Engine.checkTest(Engine.java:78)
  at einfprog.Bsp01TestFail.testWithError(Bsp01TestFail.java:91)
  at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
  at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
  ...
```
