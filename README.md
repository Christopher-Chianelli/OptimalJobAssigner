# OptimalJobAssigner
Assign people to team via Optaplanner

Compile with `mvn clean install`, run with `mvn exec:java`

# Running

First, you enter the positions avaliable, in the format "(NUMBER OF POSITIONS AVALIABLE) (POSITION TITLE)".
You end the list with an empty line.

So if you have 1 Java position and 2 C positions, you would enter
```
1 Java
2 C

```

Then, you enter the intern names and rankings. The rankings begin at 1. Positions can share a ranking.
So if we have an intern Anna who perfers Java over C, you would enter "Anna" for the intern name, "1" for
Java, and "2" for C

```
Intern Name (blank to exit):
Anna
Anna Ranking for Java:
1
Anna Ranking for C:
2
```

Optaplanner will then start assigning interns to position. An example assignment is `Intern: Position (score)`./
In this case, I used the mapping `score = -(ranking * ranking)`. You can use a different scoring scheme by
modifying the source.
