angular.module('main.controllers',[])
    .controller('modalController', function ($scope, $modalInstance, items) {

        $scope.items = items;
        $scope.selected = {
            item: $scope.items[0]
        };

        $scope.ok = function () {
            $modalInstance.close($scope.selected.item);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    })
    .controller('messageModalController', function ($scope, $modalInstance, message) {

        $scope.message = message;

        $scope.ok = function () {
            $modalInstance.close();
        };
    });
