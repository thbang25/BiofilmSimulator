# BioFilm_Simulator_BacterialGrowth

## Project Overview

This project simulates the growth and interaction of two types of bacteria Red and Blue within a biofilm environment. The bacteria exhibit different behaviors such as motility and reproduction, and they interact with randomly placed nutrients that enhance their growth. The environment is modeled as a grid where the bacteria can spread, reproduce, and leave behind pheromone trails (Psl trails). The simulation is managed by the BioFilmSimulator class, which stages the behavior of bacteria and nutrient interactions.


# Table of Contents

- Project Overview
- How to Run
- Class Descriptions
    - BioFilmSimulator
    - Bacteria
    - BlueBacteria
    - RedBacteria
    - Nutrient      
- Features

# How to Start Simulation

Here are the instructions:
1. Clone or download the repository.
2. Compile the project using a Java development environment (e.g., JGrasp, Eclipse, or the command line).
3. Run the BioFilmSimulator class to start the simulation.
4. You can adjust parameters such as the growth rate, number of bacteria, temperature, PH, and nutrients within the simulation.

# Class Descriptions

**BioFilmSimulator**:

The BioFilmSimulator class functions as the primary controller of the simulation, responsible for initializing the grid, placing bacteria and nutrients, and updating the simulation's state over time.

**Code Description**:
This code initializes the grid environment and places bacteria and nutrients. It also updates bacteria positions, manages bacterial reproduction, and handles interactions between bacteria and nutrients, promoting growth when bacteria encounter nutrients. Additionally, it manages the main simulation loop and user-defined parameters such as the number of bacteria, growth rates, temperature, pH, and nutrient count.

**Bacteria**:

The Bacteria class is an abstract class that defines bacteria's core functionality and behavior. Both the BlueBacteria and RedBacteria classes extend this abstract class.

**Attributes**:
- x, y : The horizontal and vertical coordinates of the bacterium in the grid.
- color: The color used to differentiate bacteria types.
- pslTrail: A list of coordinates representing the pheromone trail left by the bacteria.

**Methods**:
- update(): Abstract method implemented by subclasses to define their specific growth behavior.
- trySpread(): Attempts to spread the bacteria to a nearby space.
- AsexualReproduction(): Abstract method for bacteria reproduction.
- addTrail(): Adds the current position to the bacterium’s pheromone trail.

**BlueBacteria**:

The BlueBacteria class extends the Bacteria class and represents a non-motile bacterium that grows outward in a controlled fashion. It spreads primarily through reproduction rather than movement.

**Unique Behavior**:
Does not move like red bacteria, but reproduces by spreading to adjacent spaces, and spreads outward uniformly from the parent bacterium, simulating biofilm growth.

**Key Methods**:
- update(): Controls the growth of blue bacteria based on the provided growth rate.
- AsexualReproduction(): Creates a new BlueBacteria instance when reproduction occurs.

**RedBacteria**:

The RedBacteria class is an extension of the Bacteria class, representing a motile bacterium with the ability to both move and reproduce. This class is defined by its capacity to spread randomly and to relocate from its original position.

**Unique Behavior**:
Moves randomly across the grid, balancing outward movement with reproduction. Leaves behind a pheromone trail to indicate its path of movement.

**Key Methods**:
- update(): Handles both the movement and reproduction of red bacteria.
- spreadOutward(): Moves the red bacteria in a balanced outward direction while factoring in random motion.
- AsexualReproduction(): Creates a new RedBacteria instance when reproduction occurs.

## Nutrient
The Nutrient class represents power-ups that enhance the growth of bacteria when they come into contact. Nutrients are randomly placed on the grid, and bacteria can consume them to temporarily increase their growth rate.

**Attributes**:
- x, y: Coordinates of the nutrient in the grid.
Key Methods:
- getX(): Returns the horizontal position of the nutrient.
- getY(): Returns the vertical position of the nutrient.

# Features
- **Bacterial Growth Simulation**: Two types of bacteria, red and blue, display distinct growth behaviors, including reproduction and motility.
- **Psl Trails**: Bacteria leave behind pheromone trails that influence the movement of other bacteria.
- **Nutrient Interaction**: Nutrients serve as growth boosters for bacteria, increasing their reproduction when consumed.
- **Grid-based Environment**: The simulation operates on a grid where bacteria and nutrients are positioned and interact with each other.
- **Customizable Parameters**: The initial bacteria counts, growth rates, and nutrient count can be adjusted by users.

# Respository Link
https://gitlab.cs.uct.ac.za/smbtha002/biofilm_simulator_team167.git


# Acknowledgments
This project was developed as part of a study and research into biofilm formation and bacterial growth.

**Research name** : In-silico modeling of early-stage biofilm formation.

**Citation**: Pin Nie, Francisco Alarcon, Iván López-Montero, Belén Orgaz, Chantal
Valeriani & Massimo Pica Ciamarra (2021) In-silico modeling of early-stage biofilm formation,
Soft Materials, 19:3, 346-358.

N.P. and M.P.C. acknowledge support from the Singapore 
Ministry of Education through the Academic Research Fund 
MOE2017-T2-1-066 (S), and are grateful to the National 
Supercomputing Centre (NSCC) for providing computational 
resources.
