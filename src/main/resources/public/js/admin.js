angular.module('admin', ['ngRoute', 'ngResource', 'ui.bootstrap'])
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
            return _.isUndefined($scope.selectedItem) || _.isUndefined($scope.selectedItem.empty);
        }
    })
    .controller('ArticlesCtrl', function ($scope, $resource) {

        var Article = $resource('/admin/articles/:id', {id: '@id'});

        $scope.articles = [];
        $scope.selectedArticle = null;
        $scope.categories;
        $scope.tags;

        $scope.$watch('selectedArticle', function (selectedArticle) {
            if (selectedArticle != null) {
                $scope.categories = selectedArticle.categories == null? '' : selectedArticle.categories.join(',');
                $scope.tags = selectedArticle.tags == null? '' : selectedArticle.tags.join(',');
            }
        });

        $scope.init = function () {
            Article.query(function (articles) {
                $scope.articles = articles;
                $scope.selectedArticle = null;
            });
        };

        $scope.edit = function (article) {
            Article.get({id: article.id}, function (article) {
                $scope.selectedArticle = article;
            });
        };

        $scope.showEditForm = function () {
            return $scope.selectedArticle != null;
        }

        $scope.add = function () {
            $scope.selectedArticle = new Article();
        };

        $scope.save = function () {
            $scope.selectedArticle.categories = $scope.categories.split(',');
            $scope.selectedArticle.tags = $scope.tags.split(',');
            $scope.selectedArticle.$save().then(function (res) {
                $scope.init();
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
    });