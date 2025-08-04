# Functional testing workshop Greach 2018

## Creating the project

- in intellij idea: File \> New \> Project from version controller \> Git

    - Repository url: 'git@github.com:HelainSchoonjans/greach2018-functional-testing-workshop.git'

### [ALTERNATIVE] Cloning the project

If you don't want to create the project through intellij idea, you can clone the project through git:

- clone the repository

    git clone git@github.com:HelainSchoonjans/greach2018-functional-testing-workshop.git
    
- import in intellij idea: File \> New \> Project from existing sources

### Import as Gradle project

Please import the project as a Gradle project. 

### Java SDK

Choose java 8 as the SDK. Ensure you also have a version of groovy installed.

### Download the dependencies and created the instructions

Run `gradle build` and `gradle asciidoctor`

## Opening the instructions

- run the gradle task 'asciidoctor'
- go in build/asciidoc/html5 and open the 'index.html' file in your browser
