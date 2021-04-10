Project: foxes-and-rabbits-v2
Authors: Michael Kölling and David J. Barnes modified by Christopher Urban

This project is part of the material for chapter 10 of the book

   Objects First with Java - A Practical Introduction using BlueJ
   Sixth edition
   David J. Barnes and Michael Kölling
   Pearson Education, 2016

A predator-prey simulation involving foxes, rabbits, and bears in
an enclosed rectangular field.


This is the second version of the simulation. This version
uses inheritance through an Animal superclass.

This second version of the simulation includes a new animal already mentioned, bears.
Bears start out small and have a low reproduction rate, but they eat both foxes and rabbits.
Bears also have a hunger cap to still allow starvation. Bears do not destabilize the simulation.
It remains stable at least for the first 4000 steps in the simulation.

How to start:
    Create a Simulator object.
    Then call one of:
        + simulateOneStep - for a single step.
        + simulate - and supply a number (say 10) for that many steps.
        + runLongSimulation - for a simulation of 500 steps.
