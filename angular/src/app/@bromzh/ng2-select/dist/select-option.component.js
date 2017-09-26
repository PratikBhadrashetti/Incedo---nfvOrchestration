"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
var core_1 = require('@angular/core');
var select_component_1 = require('./select.component');
var SelectOptionComponent = (function () {
    function SelectOptionComponent(select) {
        this.select = select;
    }
    SelectOptionComponent.prototype.ngOnInit = function () {
        this.select.addOption(this);
    };
    SelectOptionComponent.prototype.ngOnDestroy = function () {
        this.select.removeOption(this);
    };
    SelectOptionComponent.prototype.onClick = function (event) {
        event.preventDefault();
        this.select.onOptionClick(this);
    };
    __decorate([
        core_1.Input(), 
        __metadata('design:type', Object)
    ], SelectOptionComponent.prototype, "value", void 0);
    __decorate([
        core_1.Input(), 
        __metadata('design:type', String)
    ], SelectOptionComponent.prototype, "text", void 0);
    __decorate([
        core_1.HostBinding('class.selected'), 
        __metadata('design:type', Boolean)
    ], SelectOptionComponent.prototype, "selected", void 0);
    __decorate([
        core_1.HostBinding('class.hidden'), 
        __metadata('design:type', Boolean)
    ], SelectOptionComponent.prototype, "hidden", void 0);
    __decorate([
        core_1.HostListener('click', ['$event']), 
        __metadata('design:type', Function), 
        __metadata('design:paramtypes', [MouseEvent]), 
        __metadata('design:returntype', void 0)
    ], SelectOptionComponent.prototype, "onClick", null);
    SelectOptionComponent = __decorate([
        core_1.Component({
            selector: 'br-select-option',
            host: {
                '[@option]': 'hidden ? "hidden" : "visible"',
            },
            template: "\n        <div class=\"cp-select-option\">\n            <br-check *ngIf=\"select.multiple\" [checked]=\"selected\" class=\"cp-select-option-check\"></br-check>\n            <div class=\"cp-select-option-content\"><ng-content></ng-content></div>\n        </div>\n    ",
            styles: ["\n        :host {\n            cursor: pointer;\n            background-color: #fff;\n            overflow: hidden;\n            border-bottom: 1px solid #333;\n        }\n        :host:hover {\n            background-color: #eee;\n        }\n        .cp-select-option {\n            display: inline-flex;\n            align-items: center;\n            padding: 0.5rem;\n        }\n        .cp-select-option-check {\n            flex: 0;\n            flex-basis: initial;\n            margin-right: 0.5rem;\n            /*padding: 0 0.5rem;*/\n        }\n        .cp-select-option-content {\n            flex: 1;\n        }\n    "],
            animations: [
                core_1.trigger('option', [
                    core_1.state('hidden', core_1.style({ opacity: 0, height: 0, display: 'none' })),
                    core_1.state('visible', core_1.style({ opacity: '*', height: '*', display: '*' })),
                    core_1.transition('visible <=> hidden', core_1.animate('250ms ease')),
                ]),
            ],
        }),
        __param(0, core_1.Host()), 
        __metadata('design:paramtypes', [select_component_1.SelectComponent])
    ], SelectOptionComponent);
    return SelectOptionComponent;
}());
exports.SelectOptionComponent = SelectOptionComponent;
//# sourceMappingURL=select-option.component.js.map