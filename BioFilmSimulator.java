/**
Team 167
Thabang, Kenneth, Omolemo
Biofilm simulator
bio film to execute the bacteria, UI and threads
*/

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Collections;

//concurrency tools
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

//file writer
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;


public class BioFilmSimulator extends JFrame {
/** created the constants for the simulator */
   private static final int WIDTH = 700; /** the width of our screen */
   private static final int HEIGHT = 700;/** the height of our screen */
   private static final int GRID_SIZE = 100;/** created the size of the Grid */
   private static final int CELL_SIZE = 6; /* the magnitude of the bacteria */
   
/** New constants for temperature */
   private static final int MIN_TEMPERATURE = 0;
   private static final int MAX_TEMPERATURE = 100;
   private static final int OPTIMAL_TEMPERATURE = 37; // Optimum for bacteria growth
   
/** instances */
   private int[][] Grid;/** the Grid for the bacteria to spread in 2d so far */
   private List<Bacteria> bacteria; /** bacteria */
   private final Random random; /** randomness of the spread */
   private final ExecutorService executorService;
   private Timer timer; /** to refresh simulator */
   private boolean isSimulating = false; /** look if its running */
   private boolean isSpreadingOnGrid = true; /* try to spread */
   
/** New instance variables for temperature */
   private JSlider temperatureSlider; // Slider for temperature
   private int currentTemperature = OPTIMAL_TEMPERATURE; // Default starting temperature
   
/** my user interface components for the user to interact with */
   private JButton startButton;/** start */
   private JButton pauseButton;/** pause */
   private JButton resumeButton;/** resume simulator */
   private JButton resetButton;/** go back to default or start */
   private JButton stopButton;/** end but ask first */
   private JTextField redBacteriaCountField;
   private JTextField blueBacteriaCountField;
   private JTextField redGrowthRateField;
   private JTextField blueGrowthRateField;
   private JPanel simulationPanel;  /* the environment */
   
/** nutrient parameters */
   private final List<Nutrient> nutrients;
   private JTextField nutrientCountField; // TextField for nutrient count
   private int nutrientCount = 5; // Default value
   
/** PH level parameters */
   private JTextField PHField; // TextField PH
   private double PH_level = 7; // Default value
   private final double optimalPH2 = 7.5;
   private final double maxPH = 14;
   
/** set the default values just in case the wrong values or the user opts to start */
   private int RedBacteriaCount = 3;
   private int BlueBacteriaCount = 3;
   private double redGrowthRate = 0.01;
   private double blueGrowthRate = 0.01;
   
/** set the adjustment variables */
   double adjustedRedGrowthRate = 0.01;
   double adjustedBlueGrowthRate = 0.01;
   
/** write data generated to a file */
   private BufferedWriter writer;

/** simulator */
   public BioFilmSimulator() {
      /* set up the screen for the biofilm */
      setTitle("Biofilm Simulation"); /* simulator name */
      setSize(WIDTH, HEIGHT);/* parameters of the screen */
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);/* end if its closed */
      setLocationRelativeTo(null);/* put it in the center */
   
      Grid = new int[GRID_SIZE][GRID_SIZE];/* the environment parameters */
      bacteria = new ArrayList<>();/* bacteria */
      nutrients = new ArrayList<>(); /* nutrients */
      random = new Random();/* randomize */
      executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); /* start the threads */
   
    /* start Interface and the environment */
      setupUI();
      initializeGrid();
   }

   private void setupUI() {
      setLayout(new BorderLayout()); /* set up the view for the environment */
   
   /* show how the interactions in the simulation */
      simulationPanel = 
         new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
               super.paintComponent(g);
               drawGrid(g); /* produce the environment */
            }
         };
         
   /* creating the desired size for the panel */
      simulationPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
      
   /* the buttons for the controls */
      JPanel controlPanel = new JPanel(new FlowLayout());
      
   /* created the panel for the accepted input */
      JPanel inputPanel = new JPanel(new GridLayout(12, 4)); //adjust how you fit inputs
   
   /* created and initialize the control buttons  */
      startButton = new JButton("Start");
      pauseButton = new JButton("Pause");
      resumeButton = new JButton("Resume");
      resetButton = new JButton("Reset");
      stopButton = new JButton("Stop");
      
   /* initialize the input spaces */
      redBacteriaCountField = new JTextField(5);  /* Set to 5 columns to make it narrow */
      blueBacteriaCountField = new JTextField(5);
      redGrowthRateField = new JTextField(5);
      blueGrowthRateField = new JTextField(5);
      nutrientCountField = new JTextField(5);
      PHField = new JTextField(5);
      
   /* Initialize and add temperature slider */
      temperatureSlider = new JSlider(JSlider.HORIZONTAL, MIN_TEMPERATURE, MAX_TEMPERATURE, currentTemperature);
      temperatureSlider.setMajorTickSpacing(10);
      temperatureSlider.setPaintTicks(true);
      temperatureSlider.setPaintLabels(true);
      //get adjusted value
      temperatureSlider.addChangeListener(e -> currentTemperature = temperatureSlider.getValue());
      
      //size
      temperatureSlider.setPreferredSize(new Dimension(300, 50)); // Adjust width as needed
      
   /* label slider */
      JLabel temperatureLabel = new JLabel("Temperature");
      
      
      /* initialize Ph slider */
      JSlider phSlider = new JSlider(JSlider.HORIZONTAL, 0, 14, 7); // Range from 0 to 14, default at neutral pH 7
      phSlider.setMajorTickSpacing(1);
      phSlider.setPaintTicks(true);
      phSlider.setPaintLabels(true);
      //get adjusted value
      phSlider.addChangeListener(e -> PH_level = phSlider.getValue());
      
       //size
      phSlider.setPreferredSize(new Dimension(80, 40)); // Adjust width as needed
      
   /* label slider */
      JLabel phLabel = new JLabel("PH Level");
      
   
   /* add the action listeners for each buttons */
      startButton.addActionListener(e -> startSimulation());
      pauseButton.addActionListener(e -> pauseSimulation());
      resumeButton.addActionListener(e -> resumeSimulation());
      resetButton.addActionListener(e -> resetSimulation());
      stopButton.addActionListener(e -> stopSimulation());
   
   /* add the buttons to the designed control panel */
      controlPanel.add(startButton);
      controlPanel.add(pauseButton);
      controlPanel.add(resumeButton);
      controlPanel.add(resetButton);
      controlPanel.add(stopButton);
      
   /* add the input and the name of the fields to the input space */
      inputPanel.add(new JLabel("Red Bacteria Count:"));
      inputPanel.add(redBacteriaCountField);
      inputPanel.add(new JLabel("Red Bacteria Growth Rate:"));
      inputPanel.add(redGrowthRateField);
      inputPanel.add(new JLabel("Blue Bacteria Count:"));
      inputPanel.add(blueBacteriaCountField);
      inputPanel.add(new JLabel("Blue Bacteria Growth Rate:"));
      inputPanel.add(blueGrowthRateField);
      
   /* nutrient parameters and fields */
      inputPanel.add(new JLabel("Nutrient Count:"));
      inputPanel.add(nutrientCountField);
      
   /* PH level */
      inputPanel.add(new JLabel("PH Level:"));
      inputPanel.add(PHField);
   
   
   /* added the button for the section at the end of the screen */
      JPanel bottomPanel = new JPanel(new BorderLayout());
      
   /* added the input panel at the west of the screen layout */
      add(inputPanel, BorderLayout.WEST); 
      
      
   /* temperature */
   /* Create a panel to hold the slider and its label */
      JPanel temperaturePanel = new JPanel();
      temperaturePanel.setLayout(new BoxLayout(temperaturePanel, BoxLayout.X_AXIS));
      temperaturePanel.add(temperatureLabel);  // Add the text label
      temperaturePanel.add(temperatureSlider); 
      add(temperaturePanel, BorderLayout.NORTH);
      
   /* PH */
   /* Create a panel to hold the slider and its label */
      JPanel phPanel = new JPanel();
      phPanel.setLayout(new BoxLayout(phPanel, BoxLayout.Y_AXIS));
      //set the orientation of the slider
      phSlider.setOrientation(JSlider.VERTICAL);
      phPanel.add(phLabel);  // Add the text label
      phPanel.add(phSlider); 
      //add to the east side of the screen
      add(phPanel, BorderLayout.EAST);
               
                  
   //add components to designated panels 
        
   /* control panel at the bottom*/
      bottomPanel.add(controlPanel, BorderLayout.SOUTH);
       
   /* simulation panel to the centre of the screen */
      add(simulationPanel, BorderLayout.CENTER);
      
    /* the screen panel to the end of the screen */
      add(bottomPanel, BorderLayout.SOUTH);
      
   /* refresh for new instances of simulation data */
      timer = new Timer(100, 
         e -> {
            updateGrid(); /* refresh environment */
            simulationPanel.repaint();
         });
   /* update the button states based on how the simulation is running at the moment */
      updateButtonStates();
   }

   private void initializeGrid() {
      /* Initialize the Grid with zeros */
      for (int i = 0; i < GRID_SIZE; i++) {
         for (int j = 0; j < GRID_SIZE; j++) {
            Grid[i][j] = 0;
         }
      }
   
      bacteria.clear();
     /* Place red bacteria */
      placeBacteria(RedBacteriaCount, 2);
      /* Place blue bacteria */
      placeBacteria(BlueBacteriaCount, 1); 
      isSpreadingOnGrid = true;}
      
   /** place nutrients around the environment */
   private void placeNutrients(int count) {
      nutrients.clear();
      int placed = 0;
      while (placed < count) {
         int x = random.nextInt(GRID_SIZE);
         int y = random.nextInt(GRID_SIZE);
         if (Grid[x][y] == 0) {
            nutrients.add(new Nutrient(x, y));
            placed++;
         }
      }
   }

/** place the bacteria in the environment */
   private void placeBacteria(int count, int type) {
      int placed = 0;
      while (placed < count) {
         int x = random.nextInt(GRID_SIZE);
         int y = random.nextInt(GRID_SIZE);
         if (Grid[x][y] == 0) {/* the environment has space */
         /* create type of bacteria */
            Bacteria b = type == 1 ? new BlueBacteria(x, y) : new RedBacteria(x, y);
            bacteria.add(b);
         /* deploy type */
            Grid[x][y] = type;
            placed++;
         }
      }
   }

   /** update environment and give real time feedback */
   private void updateGrid() {
   /*   update the grid environment */
      int[][] newGrid = new int[GRID_SIZE][GRID_SIZE];
   /*    Create a synchronized list to avoid concurrent modification */
      List<Bacteria> newBacteria = Collections.synchronizedList(new ArrayList<>(bacteria));
    
      if (isSpreadingOnGrid) {
      
        // Using concurrency to manage spread
         List<Future<Void>> futures = new ArrayList<>();
         for (Bacteria b : bacteria) {
            futures.add(executorService.submit(
               () -> {
                  b.trySpread(Grid, newBacteria, random);
                  return null;
               }));
         }
      
        // Wait for all tasks to complete
         for (Future<Void> future : futures) {
            try {
               future.get();
            } catch (InterruptedException | ExecutionException e) {
               e.printStackTrace();
            }
         }
      
        // Flag if it is not spreading
         isSpreadingOnGrid = false;
      
      } else {
        // Modify bacteria behavior based on temperature and PH
         double adjustedRedGrowthRate = adjustGrowthRateForPH(adjustGrowthRateForTemperature(redGrowthRate));
         double adjustedBlueGrowthRate = adjustGrowthRateForPH(adjustGrowthRateForTemperature(blueGrowthRate));
        
         CompletableFuture.runAsync(
            () -> { 
               try{
                  writer.write("Red Bacteria Growth Rate: " + adjustedRedGrowthRate + "\n");
                  writer.write("Blue Bacteria Growth Rate: " + adjustedBlueGrowthRate + "\n");
               }
               catch (IOException e) {
                  e.printStackTrace();
               }
            
            });
       
        
         List<Future<Void>> futures = new ArrayList<>();
         for (Bacteria b : bacteria) {
            futures.add(executorService.submit(
               () -> {
                  checkNutrientConsumption(b); // Check nutrient consumption
                  
                  b.update(Grid, newBacteria, adjustedRedGrowthRate, adjustedBlueGrowthRate, random);
                  
                  // Synchronize the removal to ensure the count is up to date
                  synchronized (bacteria) {
                     if (shouldDieDueToTemperature() || shouldDieDueToPH()) {
                        newBacteria.remove(b); // Remove from simulation
                     }
                  }
                  return null;
               }));
         }
      
        // Wait for all tasks to complete
         for (Future<Void> future : futures) {
            try {
               future.get();
            } catch (InterruptedException | ExecutionException e) {
               e.printStackTrace();
            }
         }
      }
    
    // Update the grid based on the final state of bacteria
      bacteria = new ArrayList<>(newBacteria); // Ensure bacteria list is up to date
      for (Bacteria b : bacteria) {
         newGrid[b.getX()][b.getY()] = b instanceof RedBacteria ? 2 : 1;
         
      }
        
   /* the new environment */
      Grid = newGrid;
   
      CompletableFuture.runAsync(
            () -> {
            // Periodically write simulation data to the file
               try {
                  writer.write("Current Temperature: " + currentTemperature + "\n");
                  writer.write("pH Level: " + PH_level + "\n");
                  writer.write("Red Bacteria Count: " + countBacteria(RedBacteria.class) + "\n");
                  writer.write("Blue Bacteria Count: " + countBacteria(BlueBacteria.class) + "\n");
                  writer.write("Nutrient Count: " + nutrientCount + "\n");
                  writer.write("----------------------\n");
               } catch (IOException e) {
                  e.printStackTrace();
               }
            });
         
    
            
   } //update grid
      
      
// Accurate bacteria count method
   private int countBacteria(Class<?> bacteriaType) {
      synchronized (bacteria) { // Synchronize to prevent concurrent modification issues
         return (int) bacteria.stream().filter(b -> b.getClass().equals(bacteriaType)).count();
      }
   }
         
/** temperature check and decide whether they are optimal to die */
   private double adjustGrowthRateForTemperature(double growthRate) {
      double adjustedGrowthRate = growthRate;
      if (currentTemperature <= OPTIMAL_TEMPERATURE) {
         adjustedGrowthRate *= (currentTemperature / (double) OPTIMAL_TEMPERATURE);
      } else {
         adjustedGrowthRate *= (currentTemperature - OPTIMAL_TEMPERATURE) / (double) (MAX_TEMPERATURE - OPTIMAL_TEMPERATURE);
      }
      return adjustedGrowthRate;
   }   

/** since we have PH in the system we have to determine how its parameters affect our bacteria */ 
   private double adjustGrowthRateForPH(double growthRate) {
      double adjustedGrowthRate = growthRate;
       double optimalPH1 = 6.5;
       if (PH_level >= optimalPH1 && PH_level <= optimalPH2) {
         adjustedGrowthRate *= (PH_level / optimalPH1);
      }
      //thrive in acidic environments
      else if (PH_level < optimalPH1) {
         adjustedGrowthRate *= (maxPH / PH_level);
      }
      //not well in high alkaline
      else if (PH_level > optimalPH2) {
         adjustedGrowthRate *= ((PH_level / maxPH)-0.3235);
      }
      return adjustedGrowthRate;
   }
   
  /** allow bacteria to eat nutrients displayed on screen **/
   private void checkNutrientConsumption(Bacteria b) {
      nutrients.removeIf(
         n -> {
            if (b.getX() == n.getX() && b.getY() == n.getY()) {
               if (b instanceof RedBacteria) {
                // Only adjust the growth rate for red bacteria
                  adjustedRedGrowthRate *= 1.5;
               } else if (b instanceof BlueBacteria) {
                // Only adjust the growth rate for blue bacteria
                  adjustedBlueGrowthRate *= 1.5;
               }
               nutrientCount--;
               return true; // Remove the nutrient after consumption
            }
            return false; // Keep the nutrient if it is not consumed
         });
   }
   
/** check if they should die or not */
   private boolean shouldDieDueToTemperature() {
      return random.nextDouble() < (currentTemperature - OPTIMAL_TEMPERATURE) / (double) MAX_TEMPERATURE;
   }

/** check if they should die or not */
   private boolean shouldDieDueToPH() {
   /* favor acid environments and start to fail in basic environments as in teeth */
      return random.nextDouble() < (PH_level - optimalPH2) / maxPH;
   }


/** produce the environment and its life */
   private void drawGrid(Graphics g) {
   
    /* Set background color for the grid */
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, getWidth(), getHeight());
      
     /* draw nutrients */
      g.setColor(Color.GREEN); // Nutrient color
      for (Nutrient n : nutrients) {
         g.fillOval(n.getX() * CELL_SIZE, n.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
      }
     
   /* Draw trails first to ensure they are under the bacteria */
      for (Bacteria b : bacteria) {
         g.setColor(new Color(255, 255, 0, 50)); /* set the trail to yellow to demonstrate secretion */
         for (Point p : b.getPslTrail()) {
            g.fillRect(p.x * CELL_SIZE, p.y * CELL_SIZE, CELL_SIZE, CELL_SIZE); /* Draw the trail */
         }
      } 
   
    /*  Define the width and height for the ovals */
      int ovalWidth = (int) (CELL_SIZE * 3.5); /* Elongated horizontally */
      int ovalHeight = CELL_SIZE; /* Standard height */
   
   /* create bacteria objects */
      for (Bacteria b : bacteria) {
         if (b.getX() >= 0 && b.getX() < GRID_SIZE && b.getY() >= 0 && b.getY() < GRID_SIZE) {
            if (b instanceof RedBacteria) {
            /* Draw red bacteria */
               g.setColor(new Color(255, 0, 0, 150)); /* Semi-transparent red */
               g.fillOval(b.getX() * CELL_SIZE, b.getY() * CELL_SIZE, ovalWidth, ovalHeight);
               g.setColor(Color.RED); /* Outline color */
               g.drawOval(b.getX() * CELL_SIZE, b.getY() * CELL_SIZE, ovalWidth, ovalHeight);
               
            } else if (b instanceof BlueBacteria) {
            /* Draw blue bacteria */
               g.setColor(new Color(0, 0, 255, 150)); /* Semi-transparent blue */
               g.fillOval(b.getX() * CELL_SIZE, b.getY() * CELL_SIZE, ovalWidth, ovalHeight);
               g.setColor(Color.BLUE); /* Outline color */
               g.drawOval(b.getX() * CELL_SIZE, b.getY() * CELL_SIZE, ovalWidth, ovalHeight);
            }
         }
      }
   }

/** start simulation */
   private void startSimulation() {
   
      if (!isSimulating) {
         try {
            /* Read user inputs */
            RedBacteriaCount = Integer.parseInt(redBacteriaCountField.getText());
            BlueBacteriaCount = Integer.parseInt(blueBacteriaCountField.getText());
            redGrowthRate = Double.parseDouble(redGrowthRateField.getText());
            blueGrowthRate = Double.parseDouble(blueGrowthRateField.getText());
            nutrientCount = Integer.parseInt(nutrientCountField.getText());
            PH_level = Double.parseDouble(PHField.getText());   
         
            /* Validate inputs */
            if (RedBacteriaCount < 0 || BlueBacteriaCount < 0 || redGrowthRate < 0 || blueGrowthRate < 0 || nutrientCount < 0 || PH_level < 0 || PH_level > 14) {
               throw new NumberFormatException("Values must be non-negative whole numbers for the bacteria, PH and Nutrients.");
            }
         }
         catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Starting with default values. Values must be non-negative whole numbers for the bacteria, PH and Nutrients.");}
      
         initializeGrid(); /* start environment with the updated values */
         placeNutrients(nutrientCount); // Place nutrients based on user input
         isSimulating = true; /* create running simulation flag */
         
         // Initialize file writer
         CompletableFuture.runAsync(
            () -> {
               try {
                  writer = new BufferedWriter(new FileWriter("./data/simulation_data.txt", false)); //set false to create a new file to record data for that current simulation
                  writer.write("Starting new simulation\n");
                  writer.write("Red Bacteria Growth Rate: " + redGrowthRate + "\n");
                  writer.write("Blue Bacteria Growth Rate: " + blueGrowthRate + "\n");
                  writer.write("Current Temperature: " + currentTemperature + "\n");
                  writer.write("Initial pH Level: " + PH_level + "\n");
                  writer.write("Red Bacteria Count: " + RedBacteriaCount + "\n");
                  writer.write("Blue Bacteria Count: " + BlueBacteriaCount + "\n");
                  writer.write("----------------------\n");
               } catch (IOException e) {
                  e.printStackTrace();
               }
            });
         
         
         timer.start(); /* begin the timer */
         updateButtonStates(); /* refresh the states of each button */
      }
   }

   private void pauseSimulation() {
      if (isSimulating) {
        /* simulation to temporary stop */
         isSimulating = false;
       /* stop the timer */
         timer.stop();
       /* refresh the state of each button */
         updateButtonStates();}
   }

   private void resumeSimulation() {
      if (!isSimulating) {
        /* simulation now is going to continue */
         isSimulating = true;
       /* continue timer */
         timer.start();
       /* refresh the state of each button */
         updateButtonStates();}
   }

   private void resetSimulation() {
     /* pause the simulation */
      pauseSimulation();
     /* restart environment */
      initializeGrid();
     /* paint again */
      simulationPanel.repaint();
     /* refresh the state of each button */
      updateButtonStates();
   }
   
   private void stopSimulation() {
   /* Stop the timer and exit the application */
      pauseSimulation();
      int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
      if (response == JOptionPane.YES_OPTION) {
      
         // Close the file writer
         try {
            if (writer != null) {
               writer.write("Simulation stopped.\n");
               writer.write("----------------------\n");
               writer.close();
            }
            
         //catch the error   
         } catch (IOException e) {
            e.printStackTrace();
         }
         executorService.shutdownNow(); //shutdown thread pool
         System.exit(0); /* Exit the application */
         
      } else {
      //update button states if no
         updateButtonStates();
      }
   }

   private void updateButtonStates() {
     /* refresh or update the state of each of the buttons based on the state of the simulation */
      startButton.setEnabled(!isSimulating);
      pauseButton.setEnabled(isSimulating);
      resumeButton.setEnabled(!isSimulating);
      resetButton.setEnabled(true);
      stopButton.setEnabled(true);
      redBacteriaCountField.setEnabled(!isSimulating);
      blueBacteriaCountField.setEnabled(!isSimulating);
      redGrowthRateField.setEnabled(!isSimulating);
      blueGrowthRateField.setEnabled(!isSimulating);
      nutrientCountField.setEnabled(!isSimulating);
      PHField.setEnabled(!isSimulating);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(
         () -> {
          /* create the instance of the simulator */
            BioFilmSimulator simulation = new BioFilmSimulator();
         /* create the screen for the environment */
            simulation.setVisible(true);
         });
   }


}

