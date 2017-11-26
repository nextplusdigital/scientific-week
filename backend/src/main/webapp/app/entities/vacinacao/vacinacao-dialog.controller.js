(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('VacinacaoDialogController', VacinacaoDialogController);

    VacinacaoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Vacinacao', 'Pessoa'];

    function VacinacaoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Vacinacao, Pessoa) {
        var vm = this;

        vm.vacinacao = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.pessoas = Pessoa.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vacinacao.id !== null) {
                Vacinacao.update(vm.vacinacao, onSaveSuccess, onSaveError);
            } else {
                Vacinacao.save(vm.vacinacao, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('romaninisistemasApp:vacinacaoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.data = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
