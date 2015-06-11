angular.module('admin.controllers')
    .controller('statisticsController', function ($scope, httpService, gameService, rangeService) {

        gameService.parameters.then(function(result) {
            $scope.params = result;
            $scope.ALL_MEMBERS = result.ALL_MEMBERS;
            $scope.PLAYERS_AMOUNT = result.PLAYERS_AMOUNT;
            $scope.SEASONS = result.SEASONS;
            $scope.ROLES = result.ROLES;
            $scope.RESULTS = result.RESULTS;
            $scope.ALL_WITH_ROLES = ['ALL'].concat(result.ROLES);
            $scope.positionRole = $scope.ALL_WITH_ROLES[0];
            $scope.lifeRole = $scope.ALL_WITH_ROLES[0];
            $scope.LIVES = result.LIVES;
            $scope.season = $scope.SEASONS[$scope.SEASONS.length - 1];
        });

        $scope.showStatistics = function(nickname, season) {
            resetStatistics();
            httpService.get("statistics", {nickname: nickname, season: season},
                function(memberStatistics) {
                    showBaseByRoleStatistics($scope.positionRole, $scope.statistics.positions,
                        memberStatistics.positions, [1,2,3,4,5,6,7,8,9,10]);
                    showBaseStatistics($scope.statistics.roles, memberStatistics.roles, $scope.ROLES);
                    showBaseByRoleStatistics($scope.lifeRole, $scope.statistics.lives, memberStatistics.lives, $scope.LIVES);
                    showBaseStatistics($scope.statistics.results, memberStatistics.results, $scope.RESULTS);
                    switchToAll();
                });
        };

        var showBaseByRoleStatistics = function(choice, chart, data, labels) {
            showBaseStatistics(chart, data, labels);
            chart['ALL'] = chart.data;
            angular.forEach($scope.ROLES , function (role, index) {
                chart[role] = [];
                chart[role].push(data.allByRole[role]);
                chart[role].push(data.winsByRole[role]);
                chart[role].push(data.clearWinsByRole[role]);
            });
        };

        var showBaseStatistics = function(chart, data, labels) {
            chart.data.push(data.all);
            chart.data.push(data.wins);
            chart.data.push(data.clearWins);
            chart.labels = labels;
        };

        var changeChartData = function(choice, chart) {
            chart.data = chart[choice];
        };

        var changePositionRole = function() {
            changeChartData($scope.positionRole, $scope.statistics.positions);
        };

        var changeLifeRole = function() {
            changeChartData($scope.lifeRole, $scope.statistics.lives);
        };

        $scope.$watch('positionRole', function() {
            changePositionRole();
        });

        $scope.$watch('lifeRole', function() {
            changeLifeRole();
        });

        var switchToAll = function() {
            $scope.positionRole == 'ALL' ? changePositionRole() : $scope.positionRole = 'ALL';
            $scope.lifeRole == 'ALL' ? changeLifeRole() : $scope.lifeRole = 'ALL';
        };

        var resetStatistics = function() {
            $scope.statistics = {
                positions: {
                    data: [],
                    labels: []
                },
                roles: {
                    data: [],
                    labels: []
                },
                lives: {
                    data: [],
                    labels: []
                },
                results: {
                    data: [],
                    labels: []
                }
            };
        };

        resetStatistics();

    });
