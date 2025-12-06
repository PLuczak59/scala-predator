# Jeu Prédateur-Proie en Scala

Un jeu de simulation prédateur-proie développé en Scala avec des principes de programmation fonctionnelle pure.

## Description

Ce projet implémente un jeu où le joueur contrôle une proie (rectangle vert) qui se déplace automatiquement dans la direction choisie, tout en échappant à des prédateurs (cercles rouges) qui le poursuivent intelligemment. Les prédateurs utilisent un algorithme du plus court chemin (BFS) pour traquer la proie sur un terrain torique.

## Fonctionnalités

- **Contrôle clavier continu** : Utilisez les flèches directionnelles pour changer la direction de la proie
- **Déplacement automatique** : La proie se déplace continuellement dans la direction choisie
- **IA prédateur** : Les prédateurs utilisent l'algorithme BFS avec voisinage de Von Neumann pour trouver le plus court chemin
- **Terrain torique** : Quand une entité sort d'un côté, elle réapparaît de l'autre
- **Détection de collision** : Collision détectée avec un rayon de 40 pixels (2 * cellSize)
- **Chronomètre en temps réel** : Format MM:SS affiché en haut à droite
- **Meilleur score de session** : Le meilleur temps est conservé durant la session de jeu
- **Système de rejeu** : Bouton "Rejouer" pour relancer une partie après Game Over

## Visualisation

- 🟢 **Proie** : Rectangle vert (20x20 pixels)
- 🔴 **Prédateurs** : Cercles rouges (rayon 10 pixels)
- ⬛ **Terrain** : Fond noir

## Architecture

### Fichiers principaux

- **Direction.scala** : Énumération des 4 directions (UP, DOWN, LEFT, RIGHT)
- **Prey.scala** : Case class de la proie avec méthodes `draw` et `move`
- **Predator.scala** : Case class du prédateur avec méthodes `draw` et `move`
- **PathFinding.scala** : Algorithme BFS pour calculer le plus court chemin
- **GameState.scala** : État immutable du jeu avec toute la logique de transformation
- **Main.scala** : Point d'entrée, interface ScalaFX et boucle de jeu

### Principes de programmation fonctionnelle

✅ **Respectés** :
- **Immutabilité** : Toutes les données sont immutables
- **Case classes** : Structures de données fonctionnelles
- **Fonctions pures** : Aucun effet de bord dans la logique métier
- **Pattern matching** : Utilisé pour les directions et les transformations
- **Collections fonctionnelles** : `map`, `filter`, `fold` sur les listes
- **Pas de var** : Uniquement des `val` et case classes immutables
- **Pas de while** : Récursivité et fonctions d'ordre supérieur
- **Pas de Unit** : Toutes les fonctions métier retournent des valeurs

## Paramètres configurables

Les paramètres suivants peuvent être modifiés dans le code :

```scala
// Dans Main.scala
private final val cycleTime: Int = 100           // Vitesse du jeu en ms
private final val numberOfPredator: Int = 3      // Nombre de prédateurs
private final val cellSize: Int = 20             // Taille des cellules en pixels
