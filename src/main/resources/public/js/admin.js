angular.module('admin', ['ngRoute', 'ngResource', 'ui.bootstrap', 'ngSanitize', 'angularFileUpload'])
    .config(function ($routeProvider) {
        $routeProvider
            .when('/', {
                controller: 'MainCtrl',
                templateUrl: '/html/fragments/main.html'
            })
            .when('/articles', {
                controller: 'ArticlesCtrl',
                templateUrl: '/html/fragments/articles.html'
            })
            .when('/videos', {
                controller: 'VideosCtrl',
                templateUrl: '/html/fragments/videos.html'
            })
            .when('/users', {
                controller: 'UsersCtrl',
                templateUrl: '/html/fragments/users.html'
            })
            .when('/property', {
                controller: 'PropertyCtrl',
                templateUrl: '/html/fragments/prop.html'
            })
            .when('/categories', {
                controller: 'CategoriesCtrl',
                templateUrl: '/html/fragments/categories.html'
            })
            .otherwise({
                redirectTo: '/'
            });
    })
    .controller('MainCtrl', function ($scope, $resource) {

        var Menu = $resource('/admin/menus/:id', {id: '@id'});

        $scope.selectedItem;
        $scope.menuItems = [
        ];

        var loadMenuItems = function () {
            Menu.query(function (menus) {
                $scope.menuItems = menus;
            });
        };

        $scope.init = function () {
            loadMenuItems();
        };

        $scope.add = function () {
            $scope.selectedItem = new Menu();
        };

        $scope.delete = function (menu) {
            menu.$delete().then(function (res) {
                $scope.init();
            });
        };

        $scope.edit = function (menuItem) {
            Menu.get({id: menuItem.id}, function (menuItem) {
                $scope.selectedItem = menuItem;
            });
        };

        $scope.save = function () {
            $scope.selectedItem.$save().then(function (res) {
                loadMenuItems();
            });
        };

        $scope.showEditForm = function () {
            return $scope.selectedItem != null;
        }
    })
    .controller('ArticlesCtrl', function ($scope, $resource, $http, globalService, FileUploader) {

        var Article = $resource('/admin/articles/:id', {id: '@id'}, {
            'draft': { url: '/admin/articles/:id/draft', method: 'POST' }
        });
        var Category = $resource('/admin/categories/:id', {id: '@id'});

        $scope.uploader = new FileUploader({
            url: '/admin/articles/uploadImage'
        });
        
        $scope.articles = [];
        $scope.selectedArticle = null;
        $scope.images = [];
        
        $scope.allCategories;
        $scope.category = null;
        $scope.tags;

        $scope.body;

        $scope.previewHTML = null;

        $scope.$watch('selectedArticle', function (selectedArticle) {
            if (selectedArticle != null) {
                $scope.tags = selectedArticle.tags == null ? '' : selectedArticle.tags.join(',');
                $scope.previewHTML = null;
                $scope.body = selectedArticle.body;


                /**
                 * Start autosaving timer
                 */
//                globalService.startTimer(function () {
//                    $scope.draft();
//                });
            } else {
//                globalService.stopTimer();
                $scope.body = null;
            }
        });
        
        $scope.init = function () {
            Article.query(function (articles) {
                $scope.articles = articles;
                $scope.selectedArticle = null;
            });

            Category.query(function (items) {
                $scope.allCategories = items;
            });
            
            $scope.uploader.onCompleteItem = updateImages;
            $scope.uploader.onCompleteAll = updateImages;
        };

        $scope.edit = function (article) {
            Article.get({id: article.id}, function (article) {
                $scope.selectedArticle = article;
                $scope.message = undefined;
                $scope.uploader.url = '/admin/articles/'+article.id+'/images';
                try {
                    $scope.category = article.categoryList[0].id;
                } catch (e) {
                    $scope.category = null;
                }
                updateImages();
            });
        };

        var updateImages = function() {
            $http.get('/admin/articles/'+$scope.selectedArticle.id+'/images').success(function(images) {
                $scope.images = images;
            });
        };
        
        $scope.showEditForm = function () {
            return $scope.selectedArticle != null;
        };

        $scope.add = function () {
            new Article().$save().then(function(article) {
                $scope.edit(article);
                Article.query(function (articles) {
                    $scope.articles = articles;
                });
            });
        };

        $scope.publish = function () {
            globalService.addMessage("Publishing...");
            save($scope.selectedArticle.$save);
        };

        $scope.draft = function () {
            save($scope.selectedArticle.$draft, "Autosaved as draft...");
        };

        var save = function (savingMethod, successMessage) {
            $scope.selectedArticle.tags = $scope.tags.split(',');
            $scope.selectedArticle.categoryList = $scope.category != null ? [
                {id: $scope.category}
            ] : [];
            $scope.selectedArticle.body = $scope.body;
            savingMethod.apply($scope.selectedArticle).then(function (res) {
                globalService.addMessage(successMessage || "Saved...");
            });
        };

        $scope.delete = function (article) {
            article.$delete().then(function (res) {
                $scope.init();
            });
        };

        $scope.open = function($event) {
            $event.preventDefault();
            $event.stopPropagation();

            $scope.opened = true;
        };

        $scope.isGridImage = function(image) {
            return $scope.selectedArticle.gridImageId == image.id;
        };

        $scope.isMainImage = function(image) {
            return $scope.selectedArticle.mainImageId == image.id;
        };

        $scope.setGridImage = function(image) {
            $scope.selectedArticle.gridImageId = image.id;
        };

        $scope.setMainImage = function(image) {
            $scope.selectedArticle.mainImageId = image.id;
        };
        
        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1
        };

        $scope.preview = function () {
            console.log('Preview generating');
            if ($scope.previewHTML == null) {
                $http.post('/admin/articles/preview', $scope.selectedArticle.body).then(function (result) {
                    $scope.previewHTML = result.data;
                });
            } else {
                $scope.previewHTML = null;
            }
        };
    }).controller('PropertyCtrl', function ($scope, $http) {

        $scope.property;

        $scope.init = function () {
            $http.get('/admin/property').success(function (property) {
                $scope.property = property;
            })
        };

        $scope.save = function () {
            $http.post('/admin/property', $scope.property).success(function (property) {
                $scope.property = property;
            });
        };
    }).controller('UsersCtrl', function ($scope, $resource) {

        var User = $resource('/admin/users/:id', {id: '@id'});

        $scope.selectedUser;
        $scope.users = [
        ];

        $scope.init = function () {
            User.query(function (users) {
                $scope.users = users;
                $scope.selectedUser = undefined;
            });
        };

        $scope.add = function () {
            $scope.selectedUser = new User();
        };

        $scope.delete = function (user) {
            user.$delete().then(function (res) {
                $scope.init();
            });
        };

        $scope.edit = function (user) {
            User.get({id: user.id}, function (user) {
                $scope.selectedUser = user;
                $scope.tags = $scope.selectedUser.tags == null ? '' : $scope.selectedUser.tags.join(',');
                try {
                    $scope.category = article.categoryList[0].id;
                } catch (e) {
                    $scope.category = null;
                }
            });
        };

        $scope.save = function () {
            $scope.selectedUser.$save().then(function (res) {
                $scope.init();
            });
        };

        $scope.showEditForm = function () {
            return !_.isUndefined($scope.selectedUser);
        }
    })
    .controller('VideosCtrl', function ($scope, $resource) {

        var Video = $resource('/admin/videos/:id', {id: '@id'});
        var Category = $resource('/admin/categories/:id', {id: '@id'});

        $scope.allCategories;
        $scope.category = null;
        $scope.tags;
        
        $scope.selectedVideo = undefined;
        $scope.videos = [
        ];

        var init = function () {
            Video.query(function (videos) {
                $scope.videos = videos;
            });
            Category.query(function (items) {
                $scope.allCategories = items;
            });
        };
        init();

        $scope.add = function () {
            $scope.selectedVideo = new Video();
        };

        $scope.delete = function (video) {
            video.$delete().then(function (res) {
                init();
            });
        };

        $scope.edit = function (video) {
            Video.get({id: video.id}, function (video) {
                $scope.selectedVideo = video;
                try {
                    $scope.category = video.categoryList[0].id;
                    $scope.tags = video.tags == null ? '' : video.tags.join(',');
                } catch (e) {
                    $scope.category = null;
                }
            });
        };

        $scope.save = function () {
            $scope.selectedVideo.tags = $scope.tags.split(',');
            $scope.selectedVideo.categoryList = $scope.category != null ? [
                {id: $scope.category}
            ] : [];
            $scope.selectedVideo.$save().then(function (res) {
                init();
            });
        };

        $scope.showEditForm = function () {
            return !_.isUndefined($scope.selectedVideo);
        }
    })
    .controller('CategoriesCtrl', function ($scope, $resource) {

        var Category = $resource('/admin/categories/:id', {id: '@id'});

        $scope.selectedItem;
        $scope.items = [
        ];

        var loadItems = function () {
            Category.query(function (menus) {
                $scope.items = menus;
            });
        };

        $scope.init = function () {
            loadItems();
        };

        $scope.add = function () {
            $scope.selectedItem = new Category();
        };

        $scope.delete = function (menu) {
            menu.$delete().then(function (res) {
                $scope.init();
            });
        };

        $scope.edit = function (menuItem) {
            Category.get({id: menuItem.id}, function (menuItem) {
                $scope.selectedItem = menuItem;
            });
        };

        $scope.save = function () {
            $scope.selectedItem.$save().then(function (res) {
                loadItems();
            });
        };

        $scope.showEditForm = function () {
            return $scope.selectedItem != null;
        }
    }).factory('globalService', function($rootScope) {
        var timer = null;
        var stop = function () {
            if (timer != null) {
                clearInterval(timer);
            }
        };

        $rootScope.$on ("$routeChangeStart", function() {
            stop(timer);
        });

        var messages = [];

        return {
            startTimer: function(func) {
                stop(timer);
                timer = setInterval(function () {
                    func();
                }, 5000);
            },
            stopTimer: function() {
                stop(timer);
            },
            messages: function() {
                return messages;
            },
            addMessage: function(message) {
                messages.push({text: message});
                setTimeout(function() {
                    messages.shift();
                    $rootScope.$apply();
                }, 2000);
            }
        };
    }).controller('NotificationCtrl', function($scope, globalService) {
        $scope.messages = globalService.messages();
    });