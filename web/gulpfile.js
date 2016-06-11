const gulp = require('gulp');
const browsersync = require('browser-sync').create();
const url = require('url');
const proxyMiddleware = require('http-proxy-middleware');
const useref = require('gulp-useref');
const clean = require('gulp-clean');
const imagemin = require('gulp-imagemin');
const watch = require('gulp-watch');
const runSequence = require('run-sequence');


gulp.task('serve', function() {
    var localProxy = proxyMiddleware('/Bankaroo/api/', {target: 'http://localhost:9080'});
    browsersync.init({
        port: 3000,
        server: {
            baseDir: "./dist",
            middleware: [localProxy]
        }
    });
});

gulp.task('serve:remote', function() {
    var remoteProxy = proxyMiddleware('/Bankaroo/api/', {target: 'http://192.86.32.54:9080'});
    browsersync.init({
        port: 3000,
        server: {
            baseDir: "./dist",
            middleware: [remoteProxy]
        }
    });
});

gulp.task('clean', function () {
    return gulp.src("./dist", {read: false})
        .pipe(clean());
});

gulp.task('build:concat', function () {
    return gulp.src('./*.html')
        .pipe(useref())
        .pipe(gulp.dest('./dist'))
        .pipe(browsersync.reload({ stream:true }))
});

gulp.task('build:views', function () {
    return gulp.src('./views/**/*.html')
        .pipe(gulp.dest('./dist/views'))
        .pipe(browsersync.reload({ stream:true }))
});

gulp.task('build:images', function () {
    return gulp.src('./img/**')
        .pipe(gulp.dest('./dist/img'))
        .pipe(browsersync.reload({ stream:true }))
});

gulp.task('build:assets' , function () {
   return gulp.src('./bower_components/semantic/dist/themes/default/assets/**')
       .pipe(gulp.dest('./dist/css/themes/default/assets'))
});

gulp.task('build', function () {
    runSequence('clean', ['build:concat', 'build:views', 'build:images', 'build:assets'])
});

gulp.task('watch', function () {
    gulp.watch(['./*.html', 'css/*.css', 'app.js', 'scripts/**/*.js'], ['build:concat']);
    gulp.watch(['./views/**/*.html'], ['build:views']);
});

gulp.task('dev', function () {
    runSequence('build', 'serve', 'watch')
});

gulp.task('remote', function () {
    runSequence('build', 'serve:remote', 'watch')
});

gulp.task('default', ['dev']);