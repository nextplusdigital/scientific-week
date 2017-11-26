(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('TipoAnimalDialogController', TipoAnimalDialogController);

    TipoAnimalDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TipoAnimal'];

    function TipoAnimalDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TipoAnimal) {
        var vm = this;

        vm.tipoAnimal = entity;
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
            if (vm.tipoAnimal.id !== null) {
                TipoAnimal.update(vm.tipoAnimal, onSaveSuccess, onSaveError);
            } else {
                TipoAnimal.save(vm.tipoAnimal, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('romaninisistemasApp:tipoAnimalUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
