grails.plugin.springsecurity.filterChain.chainMap = [

        //Stateless chain
        [
                pattern: '/products',
                filters: 'none'
        ],//Stateless chain
        [
                pattern: '/notification',
                filters: 'none'
        ],//Stateless chain
        [
                pattern: '/secured/**',
                filters: 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'
        ],

        //Traditional, stateful chain
        [
                pattern: '/stateful/**',
                filters: 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter'
        ]
]


//grails.plugin.springsecurity.rest.token.validation.enableAnonymousAccess = true
