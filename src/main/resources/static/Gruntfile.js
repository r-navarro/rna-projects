module.exports = function(grunt) {

    grunt.initConfig({
        jshint: {
            files: ['Gruntfile.js', 'app.js', 'components/**/*.js', 'shared/**/*.js'],
            options: {
                globals: {
                    jQuery: true
                }
            }
        },
        jsbeautifier: {
            files: ['Gruntfile.js', 'app.js', 'components/**/*.js', 'shared/**/*.js'],
            options: {}
        },
        ngAnnotate: {
            demo: {
                files: [{
                    expand: true,
                    src: ['app.js', 'components/**/*.js', 'shared/**/*.js'],
                    ext: '.js', // Dest filepaths will have this extension.
                    extDot: 'last', // Extensions in filenames begin after the last dot
                }, ],
            }
        },
        concat: {
            dist: {
                src: ['app.js', 'components/**/*.js', 'shared/**/*.js'],
                dest: 'dest/app.js'
            }
        },
        uglify: {
            my_target: {
                files: {
                    'dest/output.min.js': ['dest/app.js']
                }
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-ng-annotate');
    grunt.loadNpmTasks("grunt-jsbeautifier");
    grunt.loadNpmTasks('grunt-contrib-uglify');

    grunt.registerTask('make', ['jshint', 'jsbeautifier', 'ngAnnotate', 'concat', 'uglify']);

};
