angular.module('app', []).controller('indexController', function ($scope, $http) {

    const contextPath = 'http://localhost:8082/api/v1';
    console.log("contextPath: " + contextPath);

    $scope.logout = function () {
        const url = '/logout';
        console.log("Method logout(), url: " + url);
        $http.get(url);
    };

    $scope.getCurrentRate = function () {
        const url = contextPath + '/rate';
        console.log("Method getCurrentRate(), url: " + url);
        $http.get(url)
                .then(function (resp) {
                    $scope.Rates = resp.data;
                });
    };

    $scope.getConvert = function () {
        const url = contextPath + '/convert';
        console.log("Method getConvert(), url: " + url);
        $http.get(url)
                .then(function (resp) {
                    $scope.Converts = resp.data;
                });
    };

    $scope.getStatistics = function () {
        const url = contextPath + '/statistics';
        console.log("Method getStatistics(), url: " + url);
        $http.get(url)
                .then(function (resp) {
                    $scope.Statistics = resp.data;
                });
    };

    $scope.fillPage = function () {
        $scope.getCurrentRate();
        $scope.getConvert();
        $scope.getStatistics();
    };

    $scope.doConvert = function () {
        const url = contextPath + '/convert';
        console.log("Method doConvert(), url: " + url);
        console.log($scope.NewConvert);

        if ($scope.NewConvert === undefined) {
            $scope.NewConvert = {}; // пустой объект
            console.log($scope.NewConvert);
        }

        $http.post(url, $scope.NewConvert)
                .then(function (resp) {
                    $scope.Result = resp.data;
                    $scope.fillPage();
                });
    };

    $scope.fillPage();

});
