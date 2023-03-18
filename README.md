# Pizza Topping Service

## Coding Challenge Questions

### If you had to build an app from scratch, what would be your ideal tech stack?

My ideal tech stack would be Kotlin for the language, Spring Boot for the framework, Postgres for a data stores (unless the use case lends itself to a particular variety of NoSQL data store), jUnit for unit testing, Cucumber for smoke testing, Kubernetes for configuring resources, and a cloud based environment.   

### What makes you a great back end engineer, and why?

There are many qualities which make for great back end engineers. There is a great deal involved with back end engineering, which requires an extensive breadth and depth of skills.

One of the qualities which serves me well as a back end engineer is a love of quality. I believe quality is essential for software systems. As powerful as distributed systems are, they are incredibly fragile. While a focus on quality at every layer doesn't guarantee a lack of failures, it does go a long way towards preventing them. When operating at scale, the impact of mistakes are amplified a thousandfold, sometimes more. One bug deployed to production could negatively impact thousands of people or more, even if caught quickly. Because of this sensitivity, a passion for quality is quite meaningful. Another reason this matters is that there is always potential for improving quality in back end systems. In practice, this passion translates during code reviews, design reviews, testing, and release management.

The constant flow of information and change from all sides in the software engineering world can be quite overwhelming. While I'm not immune to this, I can absorb a great deal of information from many sources quickly. This is helpful for keeping up with current tools and practices. It's also helpful for having an understanding of the broader systems and domains of a product. Back end systems are often microcosmic universes of their own. Often, it is quite beneficial to have a broader understanding of the different pieces and how they interact. My ability to quickly learn and apply information allows me to leverage these benefits. Every day is filled with countless opportunities to learn. I seek as many of these opportunities as possible.

Perhaps one of the most important components of a back end engineer is a collaborative spirit: the humble nature to know there is much to learn and the willingness to offer knowledge and assistance when called for; understanding that no one person holds all the knowledge, skills, and qualities required to build the best possible software system; the conviction that with enough voices harmonizing you can almost hear a software system sing. Forgive the colorful language, but these are beliefs I hold. Why do these beliefs make me a great engineer? We often lament knowledge silos, but we can avoid and reduce them with a collaborative spirit of learning. We tire of cycles of blame which run us in circles, but collaboration gives us a path forward. We despise our dependencies and the pain they bring us, but with a collaborative spirit, we see how our dependencies are empowering us and seek to form effective partnerships. Day to day, this shows itself through me being a team player, gathering input from others, and sharing my own input where it might be helpful.

These three qualities make me a great back engineer: a love of quality, a thirst for knowledge, and a spirit of collaboration.

## Running the Service

Simply press run if you're using an IDE. The source and target java versions are 17. Alternatively, the service can be run from the command line using `./gradlew bootRun`.

I chose to use an embedded in-memory database for ease of running the service. The data will be persisted across runs through file storage.

## Using the Service

While the service is running, you can view the contents of the database if you would like by accessing http://localhost:8080/h2-console in your browser. The username will be `sa`. The password will be `password` (only the best security!). The url will be `jdbc:h2:file:/data/demo`.

Swagger API docs are available at http://localhost:8080/swagger-ui/index.html while the service is running. The page can also be used to interact with the API.