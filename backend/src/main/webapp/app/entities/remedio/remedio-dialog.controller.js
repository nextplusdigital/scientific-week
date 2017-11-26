(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('RemedioDialogController', RemedioDialogController);

    RemedioDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Remedio'];

    function RemedioDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Remedio) {
        var vm = this;

        vm.remedio = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.remedio.id !== null) {
                Remedio.update(vm.remedio, onSaveSuccess, onSaveError);
            } else {
                Remedio.save(vm.remedio, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('romaninisistemasApp:remedioUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
