var gulp = require('gulp');
var connect = require('gulp-connect');
var watch = watch = require('gulp-watch');

gulp.task('dev', function() {
    connect.server({
        livereload: true
    });
});

gulp.task('reload', function() {
   connect.reload();
});

gulp.task('watch', function() {
    gulp.watch('css/*.css', ['reload']);
    gulp.watch('/*.html', ['reload']);
    gulp.watch('js/*.js', ['reload']);
});

gulp.task('default', ['dev', 'watch']);