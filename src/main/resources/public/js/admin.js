angular.module('admin', ['ngRoute', 'ngResource', 'ui.bootstrap', 'ngSanitize'])
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
    .controller('ArticlesCtrl', function ($scope, $resource, $http) {

        var Article = $resource('/admin/articles/:id', {id: '@id'}, {
            'draft': { url: '/admin/articles/:id/draft', method: 'POST' }
        });
        var Category = $resource('/admin/categories/:id', {id: '@id'});

        $scope.articles = [];
        $scope.selectedArticle = null;

        $scope.allCategories;

        $scope.category = null;

        $scope.tags;
        $scope.message;

        $scope.previewHTML = null;

        $scope.$watch('selectedArticle', function (selectedArticle) {
            if (selectedArticle != null) {
                //$scope.categories = selectedArticle.categories == null? '' : selectedArticle.categories.join(',');
                $scope.tags = selectedArticle.tags == null? '' : selectedArticle.tags.join(',');
                $scope.previewHTML = null;
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
        };

        $scope.edit = function (article) {
            Article.get({id: article.id}, function (article) {
                $scope.selectedArticle = article;
                $scope.message = undefined;
                try {
                    $scope.category = article.categoryList[0].id;
                } catch (e) {
                    $scope.category = null;
                }
            });
        };

        $scope.showEditForm = function () {
            return $scope.selectedArticle != null;
        };

        $scope.add = function () {
            $scope.selectedArticle = new Article();
        };

        $scope.publish = function () {
            save($scope.selectedArticle.$save);
        };

        $scope.draft = function () {
            save($scope.selectedArticle.$draft);
        };

        var save = function (savingMethod) {
            $scope.selectedArticle.tags = $scope.tags.split(',');
            $scope.selectedArticle.categoryList = $scope.category != null ? [
                {id: $scope.category}
            ] : [];
            $scope.message = "Saving...";
            savingMethod.apply($scope.selectedArticle).then(function (res) {
                $scope.message = "Ok";
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
    }).controller('CategoriesCtrl', function ($scope, $resource) {

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
    });